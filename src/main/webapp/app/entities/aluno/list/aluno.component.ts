import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAluno } from '../aluno.model';
import { AlunoService } from '../service/aluno.service';
import { AlunoDeleteDialogComponent } from '../delete/aluno-delete-dialog.component';

@Component({
  selector: 'jhi-aluno',
  templateUrl: './aluno.component.html',
})
export class AlunoComponent implements OnInit {
  alunos?: IAluno[];
  isLoading = false;

  constructor(protected alunoService: AlunoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.alunoService.query().subscribe(
      (res: HttpResponse<IAluno[]>) => {
        this.isLoading = false;
        this.alunos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAluno): number {
    return item.id!;
  }

  delete(aluno: IAluno): void {
    const modalRef = this.modalService.open(AlunoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aluno = aluno;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
