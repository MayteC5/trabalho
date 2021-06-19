package br.com.trabalho.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Assunto.
 */
@Entity
@Table(name = "assunto")
public class Assunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "codigoassunto", nullable = false)
    private Integer codigoassunto;

    @Column(name = "nome")
    private String nome;

    @Column(name = "carga")
    private Float carga;

    @NotNull
    @Column(name = "idprofessor", nullable = false)
    private Integer idprofessor;

    @ManyToMany(mappedBy = "assuntos")
    @JsonIgnoreProperties(value = { "assuntos", "aluno" }, allowSetters = true)
    private Set<Turma> turmas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assunto id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCodigoassunto() {
        return this.codigoassunto;
    }

    public Assunto codigoassunto(Integer codigoassunto) {
        this.codigoassunto = codigoassunto;
        return this;
    }

    public void setCodigoassunto(Integer codigoassunto) {
        this.codigoassunto = codigoassunto;
    }

    public String getNome() {
        return this.nome;
    }

    public Assunto nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getCarga() {
        return this.carga;
    }

    public Assunto carga(Float carga) {
        this.carga = carga;
        return this;
    }

    public void setCarga(Float carga) {
        this.carga = carga;
    }

    public Integer getIdprofessor() {
        return this.idprofessor;
    }

    public Assunto idprofessor(Integer idprofessor) {
        this.idprofessor = idprofessor;
        return this;
    }

    public void setIdprofessor(Integer idprofessor) {
        this.idprofessor = idprofessor;
    }

    public Set<Turma> getTurmas() {
        return this.turmas;
    }

    public Assunto turmas(Set<Turma> turmas) {
        this.setTurmas(turmas);
        return this;
    }

    public Assunto addTurma(Turma turma) {
        this.turmas.add(turma);
        turma.getAssuntos().add(this);
        return this;
    }

    public Assunto removeTurma(Turma turma) {
        this.turmas.remove(turma);
        turma.getAssuntos().remove(this);
        return this;
    }

    public void setTurmas(Set<Turma> turmas) {
        if (this.turmas != null) {
            this.turmas.forEach(i -> i.removeAssuntos(this));
        }
        if (turmas != null) {
            turmas.forEach(i -> i.addAssuntos(this));
        }
        this.turmas = turmas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assunto)) {
            return false;
        }
        return id != null && id.equals(((Assunto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assunto{" +
            "id=" + getId() +
            ", codigoassunto=" + getCodigoassunto() +
            ", nome='" + getNome() + "'" +
            ", carga=" + getCarga() +
            ", idprofessor=" + getIdprofessor() +
            "}";
    }
}
