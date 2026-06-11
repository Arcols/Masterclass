import type { Classe } from '@/types/classes'
import classesList from '@/mocks/classesList.json'

export async function getClasses(): Promise<Classe[]> {
  return classesList
}
