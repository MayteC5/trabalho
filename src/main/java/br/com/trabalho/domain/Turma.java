package br.com.trabalho.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Turma.
 */
@Entity
@Table(name = "turma")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "codigoturma", nullable = false)
    private Integer codigoturma;

    @Column(name = "sala")
    private Integer sala;

    @Column(name = "ano")
    private ZonedDateTime ano;

    @ManyToMany
    @JoinTable(
        name = "rel_turma__assuntos",
        joinColumns = @JoinColumn(name = "turma_id"),
        inverseJoinColumns = @JoinColumn(name = "assuntos_id")
    )
    @JsonIgnoreProperties(value = { "turmas" }, allowSetters = true)
    private Set<Assunto> assuntos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "turmas" }, allowSetters = true)
    private Aluno aluno;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Turma id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCodigoturma() {
        return this.codigoturma;
    }

    public Turma codigoturma(Integer codigoturma) {
        this.codigoturma = codigoturma;
        return this;
    }

    public void setCodigoturma(Integer codigoturma) {
        this.codigoturma = codigoturma;
    }

    public Integer getSala() {
        return this.sala;
    }

    public Turma sala(Integer sala) {
        this.sala = sala;
        return this;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
    }

    public ZonedDateTime getAno() {
        return this.ano;
    }

    public Turma ano(ZonedDateTime ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(ZonedDateTime ano) {
        this.ano = ano;
    }

    public Set<Assunto> getAssuntos() {
        return this.assuntos;
    }

    public Turma assuntos(Set<Assunto> assuntos) {
        this.setAssuntos(assuntos);
        return this;
    }

    public Turma addAssuntos(Assunto assunto) {
        this.assuntos.add(assunto);
        assunto.getTurmas().add(this);
        return this;
    }

    public Turma removeAssuntos(Assunto assunto) {
        this.assuntos.remove(assunto);
        assunto.getTurmas().remove(this);
        return this;
    }

    public void setAssuntos(Set<Assunto> assuntos) {
        this.assuntos = assuntos;
    }

    public Aluno getAluno() {
        return this.aluno;
    }

    public Turma aluno(Aluno aluno) {
        this.setAluno(aluno);
        return this;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        return id != null && id.equals(((Turma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Turma{" +
            "id=" + getId() +
            ", codigoturma=" + getCodigoturma() +
            ", sala=" + getSala() +
            ", ano='" + getAno() + "'" +
            "}";
    }
}
