import { ITurma } from 'app/entities/turma/turma.model';

export interface IAssunto {
  id?: number;
  codigoassunto?: number;
  nome?: string | null;
  carga?: number | null;
  turmas?: ITurma[] | null;
}

export class Assunto implements IAssunto {
  constructor(
    public id?: number,
    public codigoassunto?: number,
    public nome?: string | null,
    public carga?: number | null,
    public turmas?: ITurma[] | null
  ) {}
}

export function getAssuntoIdentifier(assunto: IAssunto): number | undefined {
  return assunto.id;
}
