import * as dayjs from 'dayjs';
import { IAssunto } from 'app/entities/assunto/assunto.model';
import { IAluno } from 'app/entities/aluno/aluno.model';

export interface ITurma {
  id?: number;
  codigoturma?: number;
  sala?: number | null;
  ano?: dayjs.Dayjs | null;
  assuntos?: IAssunto[] | null;
  aluno?: IAluno | null;
}

export class Turma implements ITurma {
  constructor(
    public id?: number,
    public codigoturma?: number,
    public sala?: number | null,
    public ano?: dayjs.Dayjs | null,
    public assuntos?: IAssunto[] | null,
    public aluno?: IAluno | null
  ) {}
}

export function getTurmaIdentifier(turma: ITurma): number | undefined {
  return turma.id;
}
