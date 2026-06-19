import type { Classe } from '@/types/classes'

const API_BASE = 'http://localhost:8080/api/subjects'

interface SubjectResponse {
  subId: string
  subName: string
}

export async function getSubjects(): Promise<Classe[]> {
  const token = localStorage.getItem('token')
  const headers: Record<string, string> = {}
  if (token) headers['Authorization'] = `Bearer ${token}`

  const res = await fetch(API_BASE, { headers })
  if (!res.ok) throw new Error(`Erreur HTTP: ${res.status}`)

  const data: SubjectResponse[] = await res.json()
  return data.map((s) => ({
    value: s.subId,
    label: s.subName,
  }))
}
