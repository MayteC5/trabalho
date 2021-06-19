import * as dayjs from 'dayjs';
import { IAluno } from 'app/entities/aluno/aluno.model';

export interface ITurma {
  id?: number;
  codigoTurma?: number;
  sala?: number | null;
  ano?: dayjs.Dayjs | null;
  aluno?: IAluno | null;
}

export class Turma implements ITurma {
  constructor(
    public id?: number,
    public codigoTurma?: number,
    public sala?: number | null,
    public ano?: dayjs.Dayjs | null,
    public aluno?: IAluno | null
  ) {}
}

export function getTurmaIdentifier(turma: ITurma): number | undefined {
  return turma.id;
}
