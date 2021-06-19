import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAssunto, Assunto } from '../assunto.model';
import { AssuntoService } from '../service/assunto.service';

@Component({
  selector: 'jhi-assunto-update',
  templateUrl: './assunto-update.component.html',
})
export class AssuntoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigoassunto: [null, [Validators.required]],
    nome: [],
    carga: [],
  });

  constructor(protected assuntoService: AssuntoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assunto }) => {
      this.updateForm(assunto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assunto = this.createFromForm();
    if (assunto.id !== undefined) {
      this.subscribeToSaveResponse(this.assuntoService.update(assunto));
    } else {
      this.subscribeToSaveResponse(this.assuntoService.create(assunto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssunto>>): void {
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

  protected updateForm(assunto: IAssunto): void {
    this.editForm.patchValue({
      id: assunto.id,
      codigoassunto: assunto.codigoassunto,
      nome: assunto.nome,
      carga: assunto.carga,
    });
  }

  protected createFromForm(): IAssunto {
    return {
      ...new Assunto(),
      id: this.editForm.get(['id'])!.value,
      codigoassunto: this.editForm.get(['codigoassunto'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      carga: this.editForm.get(['carga'])!.value,
    };
  }
}
