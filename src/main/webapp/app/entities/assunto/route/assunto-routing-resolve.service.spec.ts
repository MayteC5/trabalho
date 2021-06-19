jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssunto, Assunto } from '../assunto.model';
import { AssuntoService } from '../service/assunto.service';

import { AssuntoRoutingResolveService } from './assunto-routing-resolve.service';

describe('Service Tests', () => {
  describe('Assunto routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AssuntoRoutingResolveService;
    let service: AssuntoService;
    let resultAssunto: IAssunto | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AssuntoRoutingResolveService);
      service = TestBed.inject(AssuntoService);
      resultAssunto = undefined;
    });

    describe('resolve', () => {
      it('should return IAssunto returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssunto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAssunto).toEqual({ id: 123 });
      });

      it('should return new IAssunto if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssunto = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAssunto).toEqual(new Assunto());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssunto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAssunto).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
