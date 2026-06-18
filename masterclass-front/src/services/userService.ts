const API_BASE_USERS = 'http://localhost:8080/api/users'

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
  const res = await fetch(`${API_BASE_USERS}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  const text = await res.text()
  if (!res.ok) throw new Error(text)
  return text
}

export async function getUserById(id: string): Promise<string> {
  const token = localStorage.getItem('token')

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE_USERS}/${id}`, {
    method: 'GET',
    headers,
  })

  if (!res.ok) throw new Error(`Erreur HTTP: ${res.status}`)
  return await res.text()
}

export interface UpdateUserPayload {
  useFirstname?: string
  useLastname?: string
  useMail?: string
  useDescription?: string
  groups?: { groId: string }[]
}

export async function updateUserById(id: string, payload: UpdateUserPayload): Promise<string> {
  const token = localStorage.getItem('token')

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE_USERS}/${id}`, {
    method: 'PUT',
    headers,
    body: JSON.stringify(payload),
  })

  if (!res.ok) throw new Error(`Erreur HTTP: ${res.status}`)
  return await res.text()
}

export async function login(email: string, password: string): Promise<string> {
  const response = await fetch(`${API_BASE_USERS}/login`, {
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
  const response = await fetch(`${API_BASE_USERS}/forgot-password`, {
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
  const response = await fetch(`${API_BASE_USERS}/reset-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ token, newPassword, confirmPassword }),
  })
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText || 'Erreur lors de la réinitialisation')
  }
}
