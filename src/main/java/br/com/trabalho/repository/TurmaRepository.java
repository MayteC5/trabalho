package br.com.trabalho.repository;

import br.com.trabalho.domain.Turma;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Turma entity.
 */
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    @Query(
        value = "select distinct turma from Turma turma left join fetch turma.assuntos",
        countQuery = "select count(distinct turma) from Turma turma"
    )
    Page<Turma> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct turma from Turma turma left join fetch turma.assuntos")
    List<Turma> findAllWithEagerRelationships();

    @Query("select turma from Turma turma left join fetch turma.assuntos where turma.id =:id")
    Optional<Turma> findOneWithEagerRelationships(@Param("id") Long id);
}
