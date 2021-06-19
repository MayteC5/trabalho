import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AssuntoService } from '../service/assunto.service';

import { AssuntoComponent } from './assunto.component';

describe('Component Tests', () => {
  describe('Assunto Management Component', () => {
    let comp: AssuntoComponent;
    let fixture: ComponentFixture<AssuntoComponent>;
    let service: AssuntoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AssuntoComponent],
      })
        .overrideTemplate(AssuntoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AssuntoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AssuntoService);

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
      expect(comp.assuntos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
