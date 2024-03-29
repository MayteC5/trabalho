import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Curso } from 'app/entities/enumerations/curso.model';
import { IAluno, Aluno } from '../aluno.model';

import { AlunoService } from './aluno.service';

describe('Service Tests', () => {
  describe('Aluno Service', () => {
    let service: AlunoService;
    let httpMock: HttpTestingController;
    let elemDefault: IAluno;
    let expectedResult: IAluno | IAluno[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AlunoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        matriculaaluno: 0,
        cpf: 'AAAAAAA',
        nome: 'AAAAAAA',
        sexo: 'AAAAAAA',
        email: 'AAAAAAA',
        cursoaluno: Curso.ENGENHARIA,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Aluno', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Aluno()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Aluno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matriculaaluno: 1,
            cpf: 'BBBBBB',
            nome: 'BBBBBB',
            sexo: 'BBBBBB',
            email: 'BBBBBB',
            cursoaluno: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Aluno', () => {
        const patchObject = Object.assign(
          {
            matriculaaluno: 1,
            cpf: 'BBBBBB',
            sexo: 'BBBBBB',
            email: 'BBBBBB',
            cursoaluno: 'BBBBBB',
          },
          new Aluno()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Aluno', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matriculaaluno: 1,
            cpf: 'BBBBBB',
            nome: 'BBBBBB',
            sexo: 'BBBBBB',
            email: 'BBBBBB',
            cursoaluno: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Aluno', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAlunoToCollectionIfMissing', () => {
        it('should add a Aluno to an empty array', () => {
          const aluno: IAluno = { id: 123 };
          expectedResult = service.addAlunoToCollectionIfMissing([], aluno);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aluno);
        });

        it('should not add a Aluno to an array that contains it', () => {
          const aluno: IAluno = { id: 123 };
          const alunoCollection: IAluno[] = [
            {
              ...aluno,
            },
            { id: 456 },
          ];
          expectedResult = service.addAlunoToCollectionIfMissing(alunoCollection, aluno);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Aluno to an array that doesn't contain it", () => {
          const aluno: IAluno = { id: 123 };
          const alunoCollection: IAluno[] = [{ id: 456 }];
          expectedResult = service.addAlunoToCollectionIfMissing(alunoCollection, aluno);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aluno);
        });

        it('should add only unique Aluno to an array', () => {
          const alunoArray: IAluno[] = [{ id: 123 }, { id: 456 }, { id: 90147 }];
          const alunoCollection: IAluno[] = [{ id: 123 }];
          expectedResult = service.addAlunoToCollectionIfMissing(alunoCollection, ...alunoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aluno: IAluno = { id: 123 };
          const aluno2: IAluno = { id: 456 };
          expectedResult = service.addAlunoToCollectionIfMissing([], aluno, aluno2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aluno);
          expect(expectedResult).toContain(aluno2);
        });

        it('should accept null and undefined values', () => {
          const aluno: IAluno = { id: 123 };
          expectedResult = service.addAlunoToCollectionIfMissing([], null, aluno, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aluno);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
