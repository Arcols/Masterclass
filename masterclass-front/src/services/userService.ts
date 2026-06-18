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

export async function forgotPassword(mail: string): Promise<void> {
  const response = await fetch(`${API_BASE}/forgot-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ mail }),
  })
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || 'Erreur lors de la demande')
  }
}

export async function resetPassword(
  token: string,
  newPassword: string,
  confirmPassword: string,
): Promise<void> {
  const response = await fetch(`${API_BASE}/reset-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ token, newPassword, confirmPassword }),
  })
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || 'Erreur lors de la réinitialisation')
  }
}
