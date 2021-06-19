jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssuntoService } from '../service/assunto.service';
import { IAssunto, Assunto } from '../assunto.model';

import { AssuntoUpdateComponent } from './assunto-update.component';

describe('Component Tests', () => {
  describe('Assunto Management Update Component', () => {
    let comp: AssuntoUpdateComponent;
    let fixture: ComponentFixture<AssuntoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let assuntoService: AssuntoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AssuntoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AssuntoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AssuntoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      assuntoService = TestBed.inject(AssuntoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const assunto: IAssunto = { id: 456 };

        activatedRoute.data = of({ assunto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(assunto));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const assunto = { id: 123 };
        spyOn(assuntoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ assunto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: assunto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(assuntoService.update).toHaveBeenCalledWith(assunto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const assunto = new Assunto();
        spyOn(assuntoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ assunto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: assunto }));
        saveSubject.complete();

        // THEN
        expect(assuntoService.create).toHaveBeenCalledWith(assunto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const assunto = { id: 123 };
        spyOn(assuntoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ assunto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(assuntoService.update).toHaveBeenCalledWith(assunto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
