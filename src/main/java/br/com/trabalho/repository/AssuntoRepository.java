package br.com.trabalho.repository;

import br.com.trabalho.domain.Assunto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Assunto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {}
