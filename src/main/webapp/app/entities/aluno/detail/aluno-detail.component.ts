import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAluno } from '../aluno.model';

@Component({
  selector: 'jhi-aluno-detail',
  templateUrl: './aluno-detail.component.html',
})
export class AlunoDetailComponent implements OnInit {
  aluno: IAluno | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aluno }) => {
      this.aluno = aluno;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
