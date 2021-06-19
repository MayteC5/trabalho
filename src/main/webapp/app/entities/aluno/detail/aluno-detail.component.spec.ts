import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlunoDetailComponent } from './aluno-detail.component';

describe('Component Tests', () => {
  describe('Aluno Management Detail Component', () => {
    let comp: AlunoDetailComponent;
    let fixture: ComponentFixture<AlunoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AlunoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aluno: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AlunoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlunoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aluno on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aluno).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
