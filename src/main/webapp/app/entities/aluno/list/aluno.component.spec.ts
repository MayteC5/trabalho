import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AlunoService } from '../service/aluno.service';

import { AlunoComponent } from './aluno.component';

describe('Component Tests', () => {
  describe('Aluno Management Component', () => {
    let comp: AlunoComponent;
    let fixture: ComponentFixture<AlunoComponent>;
    let service: AlunoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AlunoComponent],
      })
        .overrideTemplate(AlunoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlunoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AlunoService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.alunos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
