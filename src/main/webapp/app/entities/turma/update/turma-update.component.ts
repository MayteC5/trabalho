import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITurma, Turma } from '../turma.model';
import { TurmaService } from '../service/turma.service';
import { IAssunto } from 'app/entities/assunto/assunto.model';
import { AssuntoService } from 'app/entities/assunto/service/assunto.service';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';

@Component({
  selector: 'jhi-turma-update',
  templateUrl: './turma-update.component.html',
})
export class TurmaUpdateComponent implements OnInit {
  isSaving = false;

  assuntosSharedCollection: IAssunto[] = [];
  alunosSharedCollection: IAluno[] = [];

  editForm = this.fb.group({
    id: [],
    codigoturma: [null, [Validators.required]],
    sala: [],
    ano: [],
    assuntos: [],
    aluno: [],
  });

  constructor(
    protected turmaService: TurmaService,
    protected assuntoService: AssuntoService,
    protected alunoService: AlunoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turma }) => {
      if (turma.id === undefined) {
        const today = dayjs().startOf('day');
        turma.ano = today;
      }

      this.updateForm(turma);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const turma = this.createFromForm();
    if (turma.id !== undefined) {
      this.subscribeToSaveResponse(this.turmaService.update(turma));
    } else {
      this.subscribeToSaveResponse(this.turmaService.create(turma));
    }
  }

  trackAssuntoById(index: number, item: IAssunto): number {
    return item.id!;
  }

  trackAlunoById(index: number, item: IAluno): number {
    return item.id!;
  }

  getSelectedAssunto(option: IAssunto, selectedVals?: IAssunto[]): IAssunto {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITurma>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(turma: ITurma): void {
    this.editForm.patchValue({
      id: turma.id,
      codigoturma: turma.codigoturma,
      sala: turma.sala,
      ano: turma.ano ? turma.ano.format(DATE_TIME_FORMAT) : null,
      assuntos: turma.assuntos,
      aluno: turma.aluno,
    });

    this.assuntosSharedCollection = this.assuntoService.addAssuntoToCollectionIfMissing(
      this.assuntosSharedCollection,
      ...(turma.assuntos ?? [])
    );
    this.alunosSharedCollection = this.alunoService.addAlunoToCollectionIfMissing(this.alunosSharedCollection, turma.aluno);
  }

  protected loadRelationshipsOptions(): void {
    this.assuntoService
      .query()
      .pipe(map((res: HttpResponse<IAssunto[]>) => res.body ?? []))
      .pipe(
        map((assuntos: IAssunto[]) =>
          this.assuntoService.addAssuntoToCollectionIfMissing(assuntos, ...(this.editForm.get('assuntos')!.value ?? []))
        )
      )
      .subscribe((assuntos: IAssunto[]) => (this.assuntosSharedCollection = assuntos));

    this.alunoService
      .query()
      .pipe(map((res: HttpResponse<IAluno[]>) => res.body ?? []))
      .pipe(map((alunos: IAluno[]) => this.alunoService.addAlunoToCollectionIfMissing(alunos, this.editForm.get('aluno')!.value)))
      .subscribe((alunos: IAluno[]) => (this.alunosSharedCollection = alunos));
  }

  protected createFromForm(): ITurma {
    return {
      ...new Turma(),
      id: this.editForm.get(['id'])!.value,
      codigoturma: this.editForm.get(['codigoturma'])!.value,
      sala: this.editForm.get(['sala'])!.value,
      ano: this.editForm.get(['ano'])!.value ? dayjs(this.editForm.get(['ano'])!.value, DATE_TIME_FORMAT) : undefined,
      assuntos: this.editForm.get(['assuntos'])!.value,
      aluno: this.editForm.get(['aluno'])!.value,
    };
  }
}
