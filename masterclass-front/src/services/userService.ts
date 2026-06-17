const API_BASE = 'http://localhost:8080/api/users'

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
  const res = await fetch(`${API_BASE}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  const text = await res.text()
  if (!res.ok) throw new Error(text)
  return text
}

export async function login(email: string, password: string): Promise<string> {
  const response = await fetch(`${API_BASE}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ mail: email, password }),
  })

  if (!response.ok) {
    throw new Error('Email ou mot de passe incorrect')
  }

  const data = await response.json()
  localStorage.setItem('token', data.token)
  return data.token
}
