import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAluno, getAlunoIdentifier } from '../aluno.model';

export type EntityResponseType = HttpResponse<IAluno>;
export type EntityArrayResponseType = HttpResponse<IAluno[]>;

@Injectable({ providedIn: 'root' })
export class AlunoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/alunos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(aluno: IAluno): Observable<EntityResponseType> {
    return this.http.post<IAluno>(this.resourceUrl, aluno, { observe: 'response' });
  }

  update(aluno: IAluno): Observable<EntityResponseType> {
    return this.http.put<IAluno>(`${this.resourceUrl}/${getAlunoIdentifier(aluno) as number}`, aluno, { observe: 'response' });
  }

  partialUpdate(aluno: IAluno): Observable<EntityResponseType> {
    return this.http.patch<IAluno>(`${this.resourceUrl}/${getAlunoIdentifier(aluno) as number}`, aluno, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAluno>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAluno[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAlunoToCollectionIfMissing(alunoCollection: IAluno[], ...alunosToCheck: (IAluno | null | undefined)[]): IAluno[] {
    const alunos: IAluno[] = alunosToCheck.filter(isPresent);
    if (alunos.length > 0) {
      const alunoCollectionIdentifiers = alunoCollection.map(alunoItem => getAlunoIdentifier(alunoItem)!);
      const alunosToAdd = alunos.filter(alunoItem => {
        const alunoIdentifier = getAlunoIdentifier(alunoItem);
        if (alunoIdentifier == null || alunoCollectionIdentifiers.includes(alunoIdentifier)) {
          return false;
        }
        alunoCollectionIdentifiers.push(alunoIdentifier);
        return true;
      });
      return [...alunosToAdd, ...alunoCollection];
    }
    return alunoCollection;
  }
}
