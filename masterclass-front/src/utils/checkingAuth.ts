import { useRouter } from 'vue-router'
const API_BASE = 'http://localhost:8080/api/users'

export function authHeaders() {
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${localStorage.getItem('token')}`,
  }
}

export async function checkAuth(): Promise<boolean> {
  const token = localStorage.getItem('token')
  if (!token) return false

  const res = await fetch(`${API_BASE}/tokenvalidity`, {
    headers: authHeaders(),
  })
  return res.ok
}


export function useAuth() {
  const router = useRouter()

  const requireAuth = async () => {
    const valid = await checkAuth()
    if (!valid) {
      localStorage.removeItem('token')
      router.push('/login')
    }
  }

  return { requireAuth }
}
