import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'aluno',
        data: { pageTitle: 'Alunos' },
        loadChildren: () => import('./aluno/aluno.module').then(m => m.AlunoModule),
      },
      {
        path: 'turma',
        data: { pageTitle: 'Turmas' },
        loadChildren: () => import('./turma/turma.module').then(m => m.TurmaModule),
      },
      {
        path: 'assunto',
        data: { pageTitle: 'Assuntos' },
        loadChildren: () => import('./assunto/assunto.module').then(m => m.AssuntoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
