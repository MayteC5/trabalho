package br.com.trabalho.domain;

import br.com.trabalho.domain.enumeration.Curso;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Aluno.
 */
@Entity
@Table(name = "aluno")
public class Aluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "matriculaaluno", nullable = false)
    private Integer matriculaaluno;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "cursoaluno")
    private Curso cursoaluno;

    @OneToMany(mappedBy = "aluno")
    @JsonIgnoreProperties(value = { "aluno" }, allowSetters = true)
    private Set<Turma> turmas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getMatriculaaluno() {
        return this.matriculaaluno;
    }

    public Aluno matriculaaluno(Integer matriculaaluno) {
        this.matriculaaluno = matriculaaluno;
        return this;
    }

    public void setMatriculaaluno(Integer matriculaaluno) {
        this.matriculaaluno = matriculaaluno;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Aluno cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return this.nome;
    }

    public Aluno nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Aluno sexo(String sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return this.email;
    }

    public Aluno email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Curso getCursoaluno() {
        return this.cursoaluno;
    }

    public Aluno cursoaluno(Curso cursoaluno) {
        this.cursoaluno = cursoaluno;
        return this;
    }

    public void setCursoaluno(Curso cursoaluno) {
        this.cursoaluno = cursoaluno;
    }

    public Set<Turma> getTurmas() {
        return this.turmas;
    }

    public Aluno turmas(Set<Turma> turmas) {
        this.setTurmas(turmas);
        return this;
    }

    public Aluno addTurma(Turma turma) {
        this.turmas.add(turma);
        turma.setAluno(this);
        return this;
    }

    public Aluno removeTurma(Turma turma) {
        this.turmas.remove(turma);
        turma.setAluno(null);
        return this;
    }

    public void setTurmas(Set<Turma> turmas) {
        if (this.turmas != null) {
            this.turmas.forEach(i -> i.setAluno(null));
        }
        if (turmas != null) {
            turmas.forEach(i -> i.setAluno(this));
        }
        this.turmas = turmas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aluno)) {
            return false;
        }
        return id != null && id.equals(((Aluno) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aluno{" +
            "id=" + getId() +
            ", matriculaaluno=" + getMatriculaaluno() +
            ", cpf='" + getCpf() + "'" +
            ", nome='" + getNome() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", email='" + getEmail() + "'" +
            ", cursoaluno='" + getCursoaluno() + "'" +
            "}";
    }
}
