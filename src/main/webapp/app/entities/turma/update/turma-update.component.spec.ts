jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TurmaService } from '../service/turma.service';
import { ITurma, Turma } from '../turma.model';
import { IAluno } from 'app/entities/aluno/aluno.model';
import { AlunoService } from 'app/entities/aluno/service/aluno.service';

import { TurmaUpdateComponent } from './turma-update.component';

describe('Component Tests', () => {
  describe('Turma Management Update Component', () => {
    let comp: TurmaUpdateComponent;
    let fixture: ComponentFixture<TurmaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let turmaService: TurmaService;
    let alunoService: AlunoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TurmaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TurmaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TurmaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      turmaService = TestBed.inject(TurmaService);
      alunoService = TestBed.inject(AlunoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Aluno query and add missing value', () => {
        const turma: ITurma = { id: 456 };
        const aluno: IAluno = { id: 22836 };
        turma.aluno = aluno;

        const alunoCollection: IAluno[] = [{ id: 66757 }];
        spyOn(alunoService, 'query').and.returnValue(of(new HttpResponse({ body: alunoCollection })));
        const additionalAlunos = [aluno];
        const expectedCollection: IAluno[] = [...additionalAlunos, ...alunoCollection];
        spyOn(alunoService, 'addAlunoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ turma });
        comp.ngOnInit();

        expect(alunoService.query).toHaveBeenCalled();
        expect(alunoService.addAlunoToCollectionIfMissing).toHaveBeenCalledWith(alunoCollection, ...additionalAlunos);
        expect(comp.alunosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const turma: ITurma = { id: 456 };
        const aluno: IAluno = { id: 97073 };
        turma.aluno = aluno;

        activatedRoute.data = of({ turma });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(turma));
        expect(comp.alunosSharedCollection).toContain(aluno);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const turma = { id: 123 };
        spyOn(turmaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ turma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: turma }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(turmaService.update).toHaveBeenCalledWith(turma);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const turma = new Turma();
        spyOn(turmaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ turma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: turma }));
        saveSubject.complete();

        // THEN
        expect(turmaService.create).toHaveBeenCalledWith(turma);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const turma = { id: 123 };
        spyOn(turmaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ turma });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(turmaService.update).toHaveBeenCalledWith(turma);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAlunoById', () => {
        it('Should return tracked Aluno primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAlunoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
