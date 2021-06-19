import { ITurma } from 'app/entities/turma/turma.model';
import { Curso } from 'app/entities/enumerations/curso.model';

export interface IAluno {
  id?: number;
  matriculaaluno?: number;
  cpf?: string | null;
  nome?: string | null;
  sexo?: string | null;
  email?: string | null;
  cursoaluno?: Curso | null;
  turmas?: ITurma[] | null;
}

export class Aluno implements IAluno {
  constructor(
    public id?: number,
    public matriculaaluno?: number,
    public cpf?: string | null,
    public nome?: string | null,
    public sexo?: string | null,
    public email?: string | null,
    public cursoaluno?: Curso | null,
    public turmas?: ITurma[] | null
  ) {}
}

export function getAlunoIdentifier(aluno: IAluno): number | undefined {
  return aluno.id;
}
