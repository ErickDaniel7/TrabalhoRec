package com.mycompany.biblioteca.view;

import com.mycompany.biblioteca.models.Cliente;
import com.mycompany.biblioteca.models.Emprestimo;
import com.mycompany.biblioteca.models.Livro;
import com.mycompany.biblioteca.services.ClienteService;
import com.mycompany.biblioteca.services.EmprestimoService;
import com.mycompany.biblioteca.services.LivroService;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erick Daniel
 */


public class FrameControleEmprestimo extends javax.swing.JPanel {

    private ClienteService clienteService;
    private LivroService livroService;
    private EmprestimoService emprestimoService;

    private Livro livroSelecionado = new Livro();
    private Cliente clienteSelecionado = new Cliente();
    private Emprestimo emprestimoSelecionado = new Emprestimo();
    private String filtroSelecionado= "ID";
    
    public FrameControleEmprestimo(EmprestimoService emprestimoService, LivroService livroService, ClienteService clienteService) {
        initComponents();
        preparaTabela();
        this.emprestimoService = emprestimoService;
        this.clienteService = clienteService;
        this.livroService = livroService;
        preencherTabela(null);
        desabilitarBotoesDeControle();
        prepareCampoTexto();
        limparFormularios();
    }

    private void preparaTabela() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

        /*Não permitir edição direta na tabela */
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null}
                },
                new String[]{
                    "ID", "ID Cliente", "Livro", "Status", "Data de Saída", "Data de Entrega", "Devolvido"}
        ) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Integer.class,};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void preencherTabela(List<Emprestimo> emprestimosSelecionados ) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Emprestimo> emprestimos = emprestimosSelecionados;
        
        if (emprestimosSelecionados == null){
            if (clienteSelecionado.getId() == null) {
                emprestimos = emprestimoService.listar();
            }
            if (clienteSelecionado.getId() != null) {
                emprestimos = emprestimoService.buscarEmprestimoPorClienteID(clienteSelecionado.getId());
            }
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Emprestimo emprestimo : emprestimos) {
            model.addRow(new Object[]{
                emprestimo.getId().toString(),
                emprestimo.getCliente().getId().toString(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.isAtrasado() ? "Atrasado" : "OK",
                emprestimo.getDataEmprestimo().format(formatter),
                emprestimo.getDataDevolucao().format(formatter),
                emprestimo.isFinalizado() ? "Sim" : "Não"
            });
        }
    }

    private void limparFormularios() {
        clienteSelecionado = new Cliente();
        livroSelecionado = new Livro();
        emprestimoSelecionado = new Emprestimo();

        jidClienteField.setText("");
        jnomeClienteField.setText("");
        jtituloField.setText("");
        jautorField.setText("");
        janoField.setText("");
        jquantidadeField.setText("");
        jeditoraField.setText("");
        jidLivroField.setText("");
        jfiltroEmprestimoField.setText("");
        desabilitarBotoesDeControle();

        jidClienteField.setEditable(true);
        jidLivroField.setEditable(true);

        selecionaClienteButton.setEnabled(true);
        selecionaLivroButton.setEnabled(true);
    }

    private void desabilitarBotoesDeControle() {
        emprestarButton.setEnabled(false);
        devolverButton.setEnabled(false);
        limparButton.setEnabled(false);
    }

    /*Pegar id a partir da celula da tabela e chamar serviço para obter cliente especifico */
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            // Obtém os valores das células da linha selecionada com verificação de null
            String id = getCellValueAsString(jTable1, selectedRow, 0);
            selecionarEmprestimo(id);
        }
    }

    private void selecionarEmprestimo(String id) {
        if (id != null && Pattern.matches("\\d{1,}", id)) {
            Integer idEmprestimo = Integer.parseInt(id);
            emprestimoSelecionado = emprestimoService.buscarEmprestimoPorId(idEmprestimo);
            if (emprestimoSelecionado != null) {
                clienteSelecionado = clienteService.obterClientePorID(emprestimoSelecionado.getCliente().getId().toString());
                livroSelecionado = livroService.obterLivroPorId(emprestimoSelecionado.getLivro().getId());
                popularCamposLivro(livroSelecionado);
                popularCamposCliente(clienteSelecionado);
                devolverButton.setEnabled(true);
                limparButton.setEnabled(true);
                emprestarButton.setEnabled(false);
                jidClienteField.setEditable(false);
                jidLivroField.setEditable(false);
                selecionaClienteButton.setEnabled(false);
                selecionaLivroButton.setEnabled(false);
            }
        }
    }

    private String getCellValueAsString(JTable table, int row, int column) {
        Object value = table.getValueAt(row, column);
        return value != null ? value.toString() : "";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        confirmeEmprestimoDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jdataSaidaField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jdataDevolucaoField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        confirmarEmprestimoButtonDialog = new javax.swing.JButton();
        voltarButtonDialog = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        idClienteLabel = new javax.swing.JLabel();
        jidClienteField = new javax.swing.JTextField();
        idLivroLabel = new javax.swing.JLabel();
        jquantidadeField = new javax.swing.JTextField();
        jidLivroField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jnomeClienteField = new javax.swing.JTextField();
        jautorField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtituloField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jeditoraField = new javax.swing.JTextField();
        janoField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        selecionaClienteButton = new javax.swing.JButton();
        selecionaLivroButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        filtraEmprestimoButton = new javax.swing.JButton();
        jfiltroEmprestimoField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        emprestarButton = new javax.swing.JButton();
        devolverButton = new javax.swing.JButton();
        limparButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        confirmeEmprestimoDialog.setTitle("Confirmar Empréstimo");
        confirmeEmprestimoDialog.setResizable(false);

        jLabel2.setText("Data de Saída:");

        jLabel11.setText("Data de Devolução:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdataSaidaField)
                    .addComponent(jdataDevolucaoField)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdataSaidaField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdataDevolucaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        confirmarEmprestimoButtonDialog.setText("Confirmar");
        confirmarEmprestimoButtonDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarEmprestimoButtonDialogActionPerformed(evt);
            }
        });

        voltarButtonDialog.setText("Cancelar");
        voltarButtonDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltarButtonDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(confirmarEmprestimoButtonDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voltarButtonDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 154, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmarEmprestimoButtonDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voltarButtonDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout confirmeEmprestimoDialogLayout = new javax.swing.GroupLayout(confirmeEmprestimoDialog.getContentPane());
        confirmeEmprestimoDialog.getContentPane().setLayout(confirmeEmprestimoDialogLayout);
        confirmeEmprestimoDialogLayout.setHorizontalGroup(
            confirmeEmprestimoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmeEmprestimoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(confirmeEmprestimoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        confirmeEmprestimoDialogLayout.setVerticalGroup(
            confirmeEmprestimoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmeEmprestimoDialogLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        idClienteLabel.setText("Número de ID do Cliente");

        jidClienteField.setPreferredSize(new java.awt.Dimension(64, 33));

        idLivroLabel.setText("Número de ID do Livro");

        jquantidadeField.setEditable(false);
        jquantidadeField.setPreferredSize(new java.awt.Dimension(64, 33));

        jidLivroField.setPreferredSize(new java.awt.Dimension(64, 33));

        jLabel5.setText("Título");

        jLabel6.setText("Nome do Cliente");

        jnomeClienteField.setEditable(false);
        jnomeClienteField.setPreferredSize(new java.awt.Dimension(64, 33));

        jautorField.setEditable(false);
        jautorField.setPreferredSize(new java.awt.Dimension(64, 33));

        jLabel7.setText("QTD");

        jLabel8.setText("Autor");

        jtituloField.setEditable(false);
        jtituloField.setPreferredSize(new java.awt.Dimension(64, 33));

        jLabel9.setText("Ano");

        jeditoraField.setEditable(false);
        jeditoraField.setPreferredSize(new java.awt.Dimension(64, 33));

        janoField.setEditable(false);
        janoField.setPreferredSize(new java.awt.Dimension(64, 33));

        jLabel10.setText("Editora");

        selecionaClienteButton.setText("OK");
        selecionaClienteButton.setPreferredSize(new java.awt.Dimension(72, 33));
        selecionaClienteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecionaClienteButtonActionPerformed(evt);
            }
        });

        selecionaLivroButton.setText("OK");
        selecionaLivroButton.setPreferredSize(new java.awt.Dimension(72, 33));
        selecionaLivroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecionaLivroButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idLivroLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idClienteLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jautorField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtituloField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jquantidadeField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jnomeClienteField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jidClienteField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selecionaClienteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jidLivroField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selecionaLivroButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jeditoraField, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(janoField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(idClienteLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jidClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selecionaClienteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jnomeClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idLivroLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jidLivroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selecionaLivroButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtituloField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jquantidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jautorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jeditoraField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(janoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        filtraEmprestimoButton.setText("OK");
        filtraEmprestimoButton.setPreferredSize(new java.awt.Dimension(72, 33));
        filtraEmprestimoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtraEmprestimoButtonActionPerformed(evt);
            }
        });

        jfiltroEmprestimoField.setPreferredSize(new java.awt.Dimension(64, 33));

        jLabel1.setText("Filtrar Empréstimos");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "ID Cliente", "Livro", "Data Saída", "Data Entrega" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(80, 23));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mudarItemBoxHandler(evt);
            }
        });

        jLabel3.setText("Filtrar por");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jfiltroEmprestimoField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filtraEmprestimoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jfiltroEmprestimoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filtraEmprestimoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        emprestarButton.setText("Emprestar");
        emprestarButton.setPreferredSize(new java.awt.Dimension(72, 33));
        emprestarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emprestarButtonActionPerformed(evt);
            }
        });

        devolverButton.setText("Devolver");
        devolverButton.setPreferredSize(new java.awt.Dimension(72, 33));
        devolverButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devolverButtonActionPerformed(evt);
            }
        });

        limparButton.setText("Limpar");
        limparButton.setPreferredSize(new java.awt.Dimension(72, 33));
        limparButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(emprestarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(devolverButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limparButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(devolverButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(limparButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(emprestarButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selecionaClienteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecionaClienteButtonActionPerformed
        idClienteLabel.setText("Número de ID do Cliente");
            idClienteLabel.setForeground(Color.BLACK);
        if (jidClienteField.getText() != null && jidClienteField.getText().length() > 0 && Pattern.matches("^\\d{1,}", jidClienteField.getText())) {
            clienteSelecionado = clienteService.obterClientePorID(jidClienteField.getText());
            if (clienteSelecionado.getId() != null) {
                jnomeClienteField.setText(clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
                preencherTabela(null);
            }
        } else {
            idClienteLabel.setText("Número de ID do Cliente: Formato inválido");
            idClienteLabel.setForeground(Color.red);
        }
    }//GEN-LAST:event_selecionaClienteButtonActionPerformed

    private void resetLabelSelecaoClienteLivro(){
        idClienteLabel.setText("Número de ID do Cliente");
        idClienteLabel.setForeground(Color.BLACK);
        
        idLivroLabel.setText("Número de ID do Livro");
        idLivroLabel.setForeground(Color.BLACK);
    }
    private void emprestarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emprestarButtonActionPerformed
        confirmeEmprestimoDialog.pack(); // Ajusta o tamanho do diálogo para caber nos seus componentes
        confirmeEmprestimoDialog.setLocationRelativeTo(this); // Centraliza o diálogo em relação ao painel principal
        if (emprestimoSelecionado != null && emprestimoSelecionado.getId() != null) {
            confirmeEmprestimoDialog.setTitle("Confirmar devolução");
        } else {
            confirmeEmprestimoDialog.setTitle("Confirmar empréstimo");
        }
        confirmeEmprestimoDialog.setVisible(true);
        popularDatasDeSaidaDevolucao();
    }//GEN-LAST:event_emprestarButtonActionPerformed

    private void confirmarEmprestimoButtonDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarEmprestimoButtonDialogActionPerformed
        try {
            if (emprestimoSelecionado != null && emprestimoSelecionado.getId() != null) { //Então é devolução
                //
                LocalDate dataDevolucao = converteTextoParaData(jdataDevolucaoField.getText());
                emprestimoSelecionado.setDataDevolucao(dataDevolucao);
                emprestimoService.devolver(emprestimoSelecionado);
                livroSelecionado = livroService.obterLivroPorId(emprestimoSelecionado.getLivro().getId());
                popularCamposLivro(livroSelecionado);
                preencherTabela(null);
            } else {
                LocalDate dataEmprestimo = converteTextoParaData(jdataSaidaField.getText());
                LocalDate dataDevolucao = converteTextoParaData(jdataDevolucaoField.getText());
                Emprestimo e = new Emprestimo(null, clienteSelecionado, livroSelecionado, dataEmprestimo, dataDevolucao);
                emprestimoService.emprestar(e);
                livroSelecionado = livroService.obterLivroPorId(e.getLivro().getId());
                popularCamposLivro(livroSelecionado);
                preencherTabela(null);
            }
            confirmeEmprestimoDialog.dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Empréstimo", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_confirmarEmprestimoButtonDialogActionPerformed

    private void selecionaLivroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecionaLivroButtonActionPerformed
        idLivroLabel.setText("Número de ID do Livro");
        idLivroLabel.setForeground(Color.BLACK);
        if (jidLivroField.getText() != null && jidLivroField.getText().length() > 0 && jidLivroField.getText().matches("\\d+")) {
            livroSelecionado = livroService.obterLivroPorId(Integer.parseInt(jidLivroField.getText()));
            popularCamposLivro(livroSelecionado);
            if (clienteSelecionado.getId() != null && livroSelecionado.getId() != null && (emprestimoSelecionado == null || emprestimoSelecionado.getId() == null)) {
                emprestarButton.setEnabled(true);
            }
        }
        else{
            idLivroLabel.setText("Número de ID do Livro: Formato Inválido");
            idLivroLabel.setForeground(Color.red);
        }
    }//GEN-LAST:event_selecionaLivroButtonActionPerformed

    private void voltarButtonDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltarButtonDialogActionPerformed
        confirmeEmprestimoDialog.dispose();
    }//GEN-LAST:event_voltarButtonDialogActionPerformed

    private void limparButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparButtonActionPerformed
        limparFormularios();
        preencherTabela(null);
        resetLabelSelecaoClienteLivro();
    }//GEN-LAST:event_limparButtonActionPerformed

    private void devolverButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devolverButtonActionPerformed
        //TODO: chamar dialog para registrar data de devolução.
        confirmeEmprestimoDialog.pack(); // Ajusta o tamanho do diálogo para caber nos seus componentes
        confirmeEmprestimoDialog.setLocationRelativeTo(this); // Centraliza o diálogo em relação ao painel principal
        confirmeEmprestimoDialog.setVisible(true);
        confirmeEmprestimoDialog.setTitle("Confirmar devolução");
        popularDatasDeSaidaDevolucao();
    }//GEN-LAST:event_devolverButtonActionPerformed

    private void filtraEmprestimoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtraEmprestimoButtonActionPerformed
        List<Emprestimo> emprestimos = new ArrayList();
        if ("ID Cliente".equals(filtroSelecionado) && Pattern.matches("\\d", jfiltroEmprestimoField.getText())) {
            emprestimos = emprestimoService.buscarEmprestimoPorClienteID(Integer.valueOf(jfiltroEmprestimoField.getText()));
            preencherTabela(emprestimos);
        }
        if ("ID".equals(filtroSelecionado) && Pattern.matches("\\d", jfiltroEmprestimoField.getText())) {
            emprestimos.add(emprestimoService.buscarEmprestimoPorId(Integer.valueOf(jfiltroEmprestimoField.getText())));
            preencherTabela(emprestimos);
        }
        if ("Livro".equals(filtroSelecionado) ){
            emprestimos = emprestimoService.buscarEmprestimoPorTituloLivro(jfiltroEmprestimoField.getText());
            preencherTabela(emprestimos);
        }
        if ("Data Saída".equals(filtroSelecionado)){
            emprestimos = emprestimoService.buscarEmprestimoPorDataSaida(converteTextoParaData(jfiltroEmprestimoField.getText()));
            preencherTabela(emprestimos);
        }
        if ("Data Entrega".equals(filtroSelecionado)){
            emprestimos = emprestimoService.buscarEmprestimoPorDataDevolucao(converteTextoParaData(jfiltroEmprestimoField.getText()));
            preencherTabela(emprestimos);
        }
        if (jfiltroEmprestimoField.getText() == null || jfiltroEmprestimoField.getText().trim().length() == 0){
            preencherTabela(null);
        }
    }//GEN-LAST:event_filtraEmprestimoButtonActionPerformed

    private void mudarItemBoxHandler(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mudarItemBoxHandler
         filtroSelecionado = evt.getItem().toString();
    }//GEN-LAST:event_mudarItemBoxHandler
    
    private void popularDatasDeSaidaDevolucao() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataFutura = dataAtual.plusDays(14);
        if (emprestimoSelecionado != null && emprestimoSelecionado.getId() != null) {
            jdataSaidaField.setText(emprestimoSelecionado.getDataEmprestimo().format(formatter));
            jdataSaidaField.setEditable(false);
            jdataDevolucaoField.setText(dataAtual.format(formatter));
        } else {
            jdataSaidaField.setEditable(true);
            jdataSaidaField.setText(dataAtual.format(formatter));
            jdataDevolucaoField.setText(dataFutura.format(formatter));
        }
    }

    private LocalDate converteTextoParaData(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (data != null && !data.trim().isEmpty()) {
            try {
                return LocalDate.parse(data, formatter);
            } catch (DateTimeParseException e) {
                // Tratar a exceção de parsing, por exemplo, definir dataNascimento como null ou uma data padrão
                return LocalDate.MIN; // ou qualquer outra lógica desejada
            }
        }
        return LocalDate.MIN;
    }

    private void popularCamposLivro(Livro livro) {
        jidLivroField.setText(livro.getId().toString());
        jautorField.setText(livro.getAutor());
        jeditoraField.setText(livro.getEditora());
        jquantidadeField.setText(livro.getExemplaresDisponiveis().toString());
        janoField.setText(livro.getAno().toString());
        jtituloField.setText(livro.getTitulo());
    }

    private void popularCamposCliente(Cliente cliente) {
        if (cliente != null && cliente.getId() != null) {
            jnomeClienteField.setText(clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
            jidClienteField.setText(cliente.getId().toString());
        }
    }

    private void prepareCampoTexto() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFields();
            }
        };

        jidClienteField.getDocument().addDocumentListener(listener);
        jidLivroField.getDocument().addDocumentListener(listener);
        jfiltroEmprestimoField.getDocument().addDocumentListener(listener);
    }

    /*Quando o listener associado ao field é acionado esse metodo habilita ou nao os botoes de controle*/
    private void checkFields() {
        String idCliente = jidClienteField.getText();
        String idLivro = jidLivroField.getText();
        String pesquisar = jfiltroEmprestimoField.getText();

        boolean allFieldsFilled = (idCliente != null && !idCliente.trim().isEmpty())
                && (idLivro != null && !idLivro.trim().isEmpty());

        boolean oneFieldsFilled = (idCliente != null && !idCliente.trim().isEmpty())
                || (idLivro != null && !idLivro.trim().isEmpty())
                || (pesquisar != null && !pesquisar.trim().isEmpty());
        
        limparButton.setEnabled(oneFieldsFilled);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmarEmprestimoButtonDialog;
    private javax.swing.JDialog confirmeEmprestimoDialog;
    private javax.swing.JButton devolverButton;
    private javax.swing.JButton emprestarButton;
    private javax.swing.JButton filtraEmprestimoButton;
    private javax.swing.JLabel idClienteLabel;
    private javax.swing.JLabel idLivroLabel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField janoField;
    private javax.swing.JTextField jautorField;
    private javax.swing.JTextField jdataDevolucaoField;
    private javax.swing.JTextField jdataSaidaField;
    private javax.swing.JTextField jeditoraField;
    private javax.swing.JTextField jfiltroEmprestimoField;
    private javax.swing.JTextField jidClienteField;
    private javax.swing.JTextField jidLivroField;
    private javax.swing.JTextField jnomeClienteField;
    private javax.swing.JTextField jquantidadeField;
    private javax.swing.JTextField jtituloField;
    private javax.swing.JButton limparButton;
    private javax.swing.JButton selecionaClienteButton;
    private javax.swing.JButton selecionaLivroButton;
    private javax.swing.JButton voltarButtonDialog;
    // End of variables declaration//GEN-END:variables
}
