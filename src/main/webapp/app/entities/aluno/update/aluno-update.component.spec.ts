jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AlunoService } from '../service/aluno.service';
import { IAluno, Aluno } from '../aluno.model';

import { AlunoUpdateComponent } from './aluno-update.component';

describe('Component Tests', () => {
  describe('Aluno Management Update Component', () => {
    let comp: AlunoUpdateComponent;
    let fixture: ComponentFixture<AlunoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let alunoService: AlunoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AlunoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AlunoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlunoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      alunoService = TestBed.inject(AlunoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const aluno: IAluno = { id: 456 };

        activatedRoute.data = of({ aluno });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aluno));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aluno = { id: 123 };
        spyOn(alunoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aluno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aluno }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(alunoService.update).toHaveBeenCalledWith(aluno);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aluno = new Aluno();
        spyOn(alunoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aluno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aluno }));
        saveSubject.complete();

        // THEN
        expect(alunoService.create).toHaveBeenCalledWith(aluno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aluno = { id: 123 };
        spyOn(alunoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aluno });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(alunoService.update).toHaveBeenCalledWith(aluno);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
