import { getSubjects } from '@/services/subjectService'
import type { Classe } from '@/types/classes'

export async function getClasses(): Promise<Classe[]> {
  return getSubjects()
}
