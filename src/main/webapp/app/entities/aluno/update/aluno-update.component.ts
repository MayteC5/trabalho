import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAluno, Aluno } from '../aluno.model';
import { AlunoService } from '../service/aluno.service';

@Component({
  selector: 'jhi-aluno-update',
  templateUrl: './aluno-update.component.html',
})
export class AlunoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    matriculaaluno: [null, [Validators.required]],
    cpf: [],
    nome: [],
    sexo: [],
    email: [],
    cursoaluno: [],
  });

  constructor(protected alunoService: AlunoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aluno }) => {
      this.updateForm(aluno);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aluno = this.createFromForm();
    if (aluno.id !== undefined) {
      this.subscribeToSaveResponse(this.alunoService.update(aluno));
    } else {
      this.subscribeToSaveResponse(this.alunoService.create(aluno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAluno>>): void {
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

  protected updateForm(aluno: IAluno): void {
    this.editForm.patchValue({
      id: aluno.id,
      matriculaaluno: aluno.matriculaaluno,
      cpf: aluno.cpf,
      nome: aluno.nome,
      sexo: aluno.sexo,
      email: aluno.email,
      cursoaluno: aluno.cursoaluno,
    });
  }

  protected createFromForm(): IAluno {
    return {
      ...new Aluno(),
      id: this.editForm.get(['id'])!.value,
      matriculaaluno: this.editForm.get(['matriculaaluno'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      email: this.editForm.get(['email'])!.value,
      cursoaluno: this.editForm.get(['cursoaluno'])!.value,
    };
  }
}
