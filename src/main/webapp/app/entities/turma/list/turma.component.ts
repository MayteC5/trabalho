import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';
import { TurmaDeleteDialogComponent } from '../delete/turma-delete-dialog.component';

@Component({
  selector: 'jhi-turma',
  templateUrl: './turma.component.html',
})
export class TurmaComponent implements OnInit {
  turmas?: ITurma[];
  isLoading = false;

  constructor(protected turmaService: TurmaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.turmaService.query().subscribe(
      (res: HttpResponse<ITurma[]>) => {
        this.isLoading = false;
        this.turmas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITurma): number {
    return item.id!;
  }

  delete(turma: ITurma): void {
    const modalRef = this.modalService.open(TurmaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.turma = turma;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
