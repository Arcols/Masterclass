const API_BASE = 'http://localhost:8080/api'

export interface RegisterPayload {
  firstname: string
  lastname: string
  description: string
  mail: string
  password: string
  confirmPassword: string
  groupIds: string[]
}

export async function registerUser(payload: RegisterPayload): Promise<string> {
  const res = await fetch(`${API_BASE}/users/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  const text = await res.text()
  if (!res.ok) throw new Error(text)
  return text
}
