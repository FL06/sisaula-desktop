import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sistema Desktop de Gestão Acadêmica - Sisaula (Arquivo Único)
 * Contém Conexão com BD, Models, DAOs, Controllers, Views em Swing e Classe Principal.
 */
public class SisaulaApp {

    // =========================================================================
    // 1. CONEXÃO COM O BANCO DE DADOS
    // =========================================================================
    public static class FabricaConexao {
        private static final String URL = "jdbc:mysql://localhost:3306/sisaula?useSSL=false&allowPublicKeyRetrieval=true";
        private static final String USER = "root";
        private static final String PASSWORD = "root";

        public static Connection getConexao() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    // =========================================================================
    // 2. MODELOS (ENTIDADES)
    // =========================================================================
    public static class Professor {
        private Long id;
        private String nome;
        private String cpf;
        private String email;
        private String especialidade;

        public Professor() {}
        public Professor(Long id, String nome, String cpf, String email, String especialidade) {
            this.id = id;
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
            this.especialidade = especialidade;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getEspecialidade() { return especialidade; }
        public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

        @Override
        public String toString() {
            return nome + " (" + especialidade + ")";
        }
    }

    public static class Aluno {
        private Long id;
        private String nome;
        private String cpf;
        private String email;

        public Aluno() {}
        public Aluno(Long id, String nome, String cpf, String email) {
            this.id = id;
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return nome + " (CPF: " + cpf + ")";
        }
    }

    public static class Curso {
        private Long id;
        private String nome;
        private Integer cargaHoraria;
        private Double preco;
        private Professor professor;

        public Curso() {}
        public Curso(Long id, String nome, Integer cargaHoraria, Double preco, Professor professor) {
            this.id = id;
            this.nome = nome;
            this.cargaHoraria = cargaHoraria;
            this.preco = preco;
            this.professor = professor;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public Integer getCargaHoraria() { return cargaHoraria; }
        public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
        public Double getPreco() { return preco; }
        public void setPreco(Double preco) { this.preco = preco; }
        public Professor getProfessor() { return professor; }
        public void setProfessor(Professor professor) { this.professor = professor; }

        @Override
        public String toString() {
            return nome;
        }
    }

    public static class Matricula {
        private Long id;
        private Aluno aluno;
        private Curso curso;
        private String dataMatricula;
        private String status;

        public Matricula() {}
        public Matricula(Long id, Aluno aluno, Curso curso, String dataMatricula, String status) {
            this.id = id;
            this.aluno = aluno;
            this.curso = curso;
            this.dataMatricula = dataMatricula;
            this.status = status;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Aluno getAluno() { return aluno; }
        public void setAluno(Aluno aluno) { this.aluno = aluno; }
        public Curso getCurso() { return curso; }
        public void setCurso(Curso curso) { this.curso = curso; }
        public String getDataMatricula() { return dataMatricula; }
        public void setDataMatricula(String dataMatricula) { this.dataMatricula = dataMatricula; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // =========================================================================
    // 3. CAMADA DAO (DATA ACCESS OBJECT)
    // =========================================================================
    public static class ProfessorDAO {
        public void salvar(Professor p) throws SQLException {
            String sql = "INSERT INTO professor (nome, cpf, email, especialidade) VALUES (?, ?, ?, ?)";
            try (Connection conn = FabricaConexao.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, p.getNome());
                stmt.setString(2, p.getCpf());
                stmt.setString(3, p.getEmail());
                stmt.setString(4, p.getEspecialidade());
                stmt.executeUpdate();
            }
        }

        public List<Professor> listarTodos() throws SQLException {
            List<Professor> lista = new ArrayList<>();
            String sql = "SELECT * FROM professor";
            try (Connection conn = FabricaConexao.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    lista.add(new Professor(
                            rs.getLong("id_professor"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("especialidade")
                    ));
                }
            }
            return lista;
        }
    }

    public static class AlunoDAO {
        public void salvar(Aluno a) throws SQLException {
            String sql = "INSERT INTO aluno (nome, cpf, email) VALUES (?, ?, ?)";
            try (Connection conn = FabricaConexao.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, a.getNome());
                stmt.setString(2, a.getCpf());
                stmt.setString(3, a.getEmail());
                stmt.executeUpdate();
            }
        }

        public List<Aluno> listarTodos() throws SQLException {
            List<Aluno> lista = new ArrayList<>();
            String sql = "SELECT * FROM aluno";
            try (Connection conn = FabricaConexao.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    lista.add(new Aluno(
                            rs.getLong("id_aluno"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email")
                    ));
                }
            }
            return lista;
        }
    }

    public static class CursoDAO {
        public void salvar(Curso c) throws SQLException {
            String sql = "INSERT INTO curso (nome, carga_horaria, preco, id_professor) VALUES (?, ?, ?, ?)";
            try (Connection conn = FabricaConexao.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, c.getNome());
                stmt.setInt(2, c.getCargaHoraria());
                stmt.setDouble(3, c.getPreco());
                stmt.setLong(4, c.getProfessor().getId());
                stmt.executeUpdate();
            }
        }

        public List<Curso> listarTodos() throws SQLException {
            List<Curso> lista = new ArrayList<>();
            String sql = "SELECT c.*, p.nome as prof_nome, p.cpf as prof_cpf, p.email as prof_email, p.especialidade as prof_esp " +
                         "FROM curso c INNER JOIN professor p ON c.id_professor = p.id_professor";
            try (Connection conn = FabricaConexao.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Professor p = new Professor(
                            rs.getLong("id_professor"),
                            rs.getString("prof_nome"),
                            rs.getString("prof_cpf"),
                            rs.getString("prof_email"),
                            rs.getString("prof_esp")
                    );
                    Curso c = new Curso(
                            rs.getLong("id_curso"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria"),
                            rs.getDouble("preco"),
                            p
                    );
                    lista.add(c);
                }
            }
            return lista;
        }
    }

    public static class MatriculaDAO {
        public void salvar(Matricula m) throws SQLException {
            String sql = "INSERT INTO matricula (id_aluno, id_curso, data_matricula, status) VALUES (?, ?, ?, ?)";
            try (Connection conn = FabricaConexao.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, m.getAluno().getId());
                stmt.setLong(2, m.getCurso().getId());
                stmt.setString(3, m.getDataMatricula());
                stmt.setString(4, m.getStatus());
                stmt.executeUpdate();
            }
        }

        public List<Matricula> listarTodos() throws SQLException {
            List<Matricula> lista = new ArrayList<>();
            String sql = "SELECT m.id_matricula, m.data_matricula, m.status, " +
                         "a.id_aluno, a.nome as aluno_nome, a.cpf as aluno_cpf, a.email as aluno_email, " +
                         "c.id_curso, c.nome as curso_nome, c.carga_horaria, c.preco " +
                         "FROM matricula m " +
                         "INNER JOIN aluno a ON m.id_aluno = a.id_aluno " +
                         "INNER JOIN curso c ON m.id_curso = c.id_curso";
            try (Connection conn = FabricaConexao.getConexao();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Aluno a = new Aluno(
                            rs.getLong("id_aluno"),
                            rs.getString("aluno_nome"),
                            rs.getString("aluno_cpf"),
                            rs.getString("aluno_email")
                    );
                    Curso c = new Curso(
                            rs.getLong("id_curso"),
                            rs.getString("curso_nome"),
                            rs.getInt("carga_horaria"),
                            rs.getDouble("preco"),
                            null
                    );
                    Matricula m = new Matricula(
                            rs.getLong("id_matricula"),
                            a,
                            c,
                            rs.getString("data_matricula"),
                            rs.getString("status")
                    );
                    lista.add(m);
                }
            }
            return lista;
        }
    }

    // =========================================================================
    // 4. CONTROLLERS
    // =========================================================================
    public static class ProfessorController {
        private ProfessorDAO dao = new ProfessorDAO();
        public void salvar(String nome, String cpf, String email, String especialidade) throws SQLException {
            dao.salvar(new Professor(null, nome, cpf, email, especialidade));
        }
        public List<Professor> listar() throws SQLException { return dao.listarTodos(); }
    }

    public static class AlunoController {
        private AlunoDAO dao = new AlunoDAO();
        public void salvar(String nome, String cpf, String email) throws SQLException {
            dao.salvar(new Aluno(null, nome, cpf, email));
        }
        public List<Aluno> listar() throws SQLException { return dao.listarTodos(); }
    }

    public static class CursoController {
        private CursoDAO dao = new CursoDAO();
        public void salvar(String nome, Integer cargaHoraria, Double preco, Professor professor) throws SQLException {
            dao.salvar(new Curso(null, nome, cargaHoraria, preco, professor));
        }
        public List<Curso> listar() throws SQLException { return dao.listarTodos(); }
    }

    public static class MatriculaController {
        private MatriculaDAO dao = new MatriculaDAO();
        public void salvar(Aluno aluno, Curso curso, String data, String status) throws SQLException {
            dao.salvar(new Matricula(null, aluno, curso, data, status));
        }
        public List<Matricula> listar() throws SQLException { return dao.listarTodos(); }
    }

    // =========================================================================
    // 5. CAMADA VIEW (SWING)
    // =========================================================================
    public static class TelaProfessorView extends JFrame {
        private JTextField txtNome = new JTextField(12);
        private JTextField txtCpf = new JTextField(10);
        private JTextField txtEmail = new JTextField(12);
        private JTextField txtEspecialidade = new JTextField(10);
        private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "E-mail", "Especialidade"}, 0);
        private ProfessorController controller = new ProfessorController();

        public TelaProfessorView() {
            super("Gestão de Professores");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
            form.add(new JLabel("Nome:")); form.add(txtNome);
            form.add(new JLabel("CPF:")); form.add(txtCpf);
            form.add(new JLabel("E-mail:")); form.add(txtEmail);
            form.add(new JLabel("Especialidade:")); form.add(txtEspecialidade);

            JButton btnSalvar = new JButton("Salvar Professor");
            form.add(btnSalvar);

            add(form, BorderLayout.NORTH);
            add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

            btnSalvar.addActionListener((ActionEvent e) -> {
                try {
                    controller.salvar(txtNome.getText(), txtCpf.getText(), txtEmail.getText(), txtEspecialidade.getText());
                    JOptionPane.showMessageDialog(this, "Professor salvo com sucesso!");
                    carregar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });

            carregar();
            pack();
            setLocationRelativeTo(null);
        }

        private void carregar() {
            model.setRowCount(0);
            try {
                for (Professor p : controller.listar()) {
                    model.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getEmail(), p.getEspecialidade()});
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + ex.getMessage());
            }
        }
    }

    public static class TelaAlunoView extends JFrame {
        private JTextField txtNome = new JTextField(12);
        private JTextField txtCpf = new JTextField(10);
        private JTextField txtEmail = new JTextField(12);
        private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "E-mail"}, 0);
        private AlunoController controller = new AlunoController();

        public TelaAlunoView() {
            super("Gestão de Alunos");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
            form.add(new JLabel("Nome:")); form.add(txtNome);
            form.add(new JLabel("CPF:")); form.add(txtCpf);
            form.add(new JLabel("E-mail:")); form.add(txtEmail);

            JButton btnSalvar = new JButton("Salvar Aluno");
            form.add(btnSalvar);

            add(form, BorderLayout.NORTH);
            add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

            btnSalvar.addActionListener(e -> {
                try {
                    controller.salvar(txtNome.getText(), txtCpf.getText(), txtEmail.getText());
                    JOptionPane.showMessageDialog(this, "Aluno salvo!");
                    carregar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            });

            carregar();
            pack();
            setLocationRelativeTo(null);
        }

        private void carregar() {
            model.setRowCount(0);
            try {
                for (Aluno a : controller.listar()) {
                    model.addRow(new Object[]{a.getId(), a.getNome(), a.getCpf(), a.getEmail()});
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage());
            }
        }
    }

    public static class TelaCursoView extends JFrame {
        private JTextField txtNome = new JTextField(12);
        private JTextField txtCarga = new JTextField(5);
        private JTextField txtPreco = new JTextField(5);
        private JComboBox<Professor> cbProfessor = new JComboBox<>();
        private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "Carga Horária", "Preço", "Professor"}, 0);

        private CursoController cursoController = new CursoController();
        private ProfessorController professorController = new ProfessorController();

        public TelaCursoView() {
            super("Gestão de Cursos");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
            form.add(new JLabel("Nome Curso:")); form.add(txtNome);
            form.add(new JLabel("Carga Horária:")); form.add(txtCarga);
            form.add(new JLabel("Preço:")); form.add(txtPreco);
            form.add(new JLabel("Professor:")); form.add(cbProfessor);

            JButton btnSalvar = new JButton("Salvar Curso");
            form.add(btnSalvar);

            add(form, BorderLayout.NORTH);
            add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

            btnSalvar.addActionListener(e -> {
                try {
                    Professor p = (Professor) cbProfessor.getSelectedItem();
                    cursoController.salvar(txtNome.getText(), Integer.parseInt(txtCarga.getText()), Double.parseDouble(txtPreco.getText()), p);
                    JOptionPane.showMessageDialog(this, "Curso salvo!");
                    carregar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar curso: " + ex.getMessage());
                }
            });

            carregarProfessores();
            carregar();
            pack();
            setLocationRelativeTo(null);
        }

        private void carregarProfessores() {
            try {
                cbProfessor.removeAllItems();
                for (Professor p : professorController.listar()) {
                    cbProfessor.addItem(p);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar professores: " + ex.getMessage());
            }
        }

        private void carregar() {
            model.setRowCount(0);
            try {
                for (Curso c : cursoController.listar()) {
                    model.addRow(new Object[]{c.getId(), c.getNome(), c.getCargaHoraria(), c.getPreco(), c.getProfessor().getNome()});
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + ex.getMessage());
            }
        }
    }

    public static class TelaMatriculaView extends JFrame {
        private JComboBox<Aluno> cbAluno = new JComboBox<>();
        private JComboBox<Curso> cbCurso = new JComboBox<>();
        private JTextField txtData = new JTextField(10);
        private JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Ativa", "Cancelada", "Concluída"});
        private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Aluno", "Curso", "Data", "Status"}, 0);

        private MatriculaController matriculaController = new MatriculaController();
        private AlunoController alunoController = new AlunoController();
        private CursoController cursoController = new CursoController();

        public TelaMatriculaView() {
            super("Gestão de Matrículas");
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
            form.add(new JLabel("Aluno:")); form.add(cbAluno);
            form.add(new JLabel("Curso:")); form.add(cbCurso);
            form.add(new JLabel("Data (AAAA-MM-DD):")); form.add(txtData);
            form.add(new JLabel("Status:")); form.add(cbStatus);

            JButton btnSalvar = new JButton("Realizar Matrícula");
            form.add(btnSalvar);

            add(form, BorderLayout.NORTH);
            add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

            btnSalvar.addActionListener(e -> {
                try {
                    Aluno a = (Aluno) cbAluno.getSelectedItem();
                    Curso c = (Curso) cbCurso.getSelectedItem();
                    matriculaController.salvar(a, c, txtData.getText(), (String) cbStatus.getSelectedItem());
                    JOptionPane.showMessageDialog(this, "Matrícula realizada!");
                    carregar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao matricular: " + ex.getMessage());
                }
            });

            carregarCombos();
            carregar();
            pack();
            setLocationRelativeTo(null);
        }

        private void carregarCombos() {
            try {
                cbAluno.removeAllItems();
                for (Aluno a : alunoController.listar()) cbAluno.addItem(a);

                cbCurso.removeAllItems();
                for (Curso c : cursoController.listar()) cbCurso.addItem(c);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar listas: " + ex.getMessage());
            }
        }

        private void carregar() {
            model.setRowCount(0);
            try {
                for (Matricula m : matriculaController.listar()) {
                    model.addRow(new Object[]{m.getId(), m.getAluno().getNome(), m.getCurso().getNome(), m.getDataMatricula(), m.getStatus()});
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar matrículas: " + ex.getMessage());
            }
        }
    }

    public static class TelaPrincipalView extends JFrame {
        public TelaPrincipalView() {
            super("Sisaula - Sistema de Gestão Acadêmica");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JMenuBar menuBar = new JMenuBar();
            JMenu menuCadastros = new JMenu("Módulos");

            JMenuItem itemProf = new JMenuItem("Professores");
            JMenuItem itemAluno = new JMenuItem("Alunos");
            JMenuItem itemCurso = new JMenuItem("Cursos");
            JMenuItem itemMatricula = new JMenuItem("Matrículas");

            itemProf.addActionListener(e -> new TelaProfessorView().setVisible(true));
            itemAluno.addActionListener(e -> new TelaAlunoView().setVisible(true));
            itemCurso.addActionListener(e -> new TelaCursoView().setVisible(true));
            itemMatricula.addActionListener(e -> new TelaMatriculaView().setVisible(true));

            menuCadastros.add(itemProf);
            menuCadastros.add(itemAluno);
            menuCadastros.add(itemCurso);
            menuCadastros.add(itemMatricula);

            menuBar.add(menuCadastros);
            setJMenuBar(menuBar);

            JLabel lblWelcome = new JLabel("Bem-vindo ao Sisaula!", SwingConstants.CENTER);
            lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
            add(lblWelcome, BorderLayout.CENTER);
        }
    }

    // =========================================================================
    // 6. PONTO DE ENTRADA (MAIN)
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipalView().setVisible(true);
        });
    }
}
