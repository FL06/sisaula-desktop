# Sisaula - Sistema de Gestão Acadêmica e Cursos

Sistema desktop desenvolvido em Java Swing aplicando o padrão de arquitetura MVC (Model-View-Controller) e integração com banco de dados relacional.

---

## 1. Tema do Sistema
**Nome:** Sisaula - Sistema de Gestão Acadêmica  
**Descrição:** Aplicação desktop para gerenciamento de alunos, professores, cursos e matrículas de uma instituição de ensino.

---

## 2. Diagrama MER (Modelo Entidade-Relacionamento)

### Entidades e Atributos:
- **`professor`**: `id_professor` (PK), `nome`, `cpf`, `email`, `especialidade`
- **`curso`**: `id_curso` (PK), `nome`, `carga_horaria`, `preco`, `id_professor` (FK)
- **`aluno`**: `id_aluno` (PK), `nome`, `cpf`, `email`, `data_nascimento`
- **`matricula`**: `id_matricula` (PK), `data_matricula`, `status`, `id_aluno` (FK), `id_curso` (FK)

### Relacionamentos:
- **1 Professor** leciona **N Cursos** *(1:N)*
- **1 Aluno** realiza **N Matrículas** *(1:N)*
- **1 Curso** possui **N Matrículas** *(1:N)*

---

## 3. Diagrama de Classes (MVC)

```text
+------------------------+          +------------------------+
|       Professor        |          |         Curso          |
+------------------------+          +------------------------+
| - id: Long             |          | - id: Long             |
| - nome: String         | 1      * | - nome: String         |
| - cpf: String          |----------| - cargaHoraria: Integer|
| - email: String        |          | - preco: Double        |
| - especialidade: String|          | - professor: Professor |
+------------------------+          +------------------------+
                                                | 1
                                                |
                                                | *
+------------------------+          +------------------------+
|         Aluno          |          |       Matricula        |
+------------------------+          +------------------------+
| - id: Long             |          | - id: Long             |
| - nome: String         | 1      * | - dataMatricula: Date  |
| - cpf: String          |----------| - status: String       |
| - email: String        |          | - aluno: Aluno         |
| - dataNascimento: Date |          | - curso: Curso         |
+------------------------+          +------------------------+
