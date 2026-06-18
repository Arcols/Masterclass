export function useAuthToken() {
  const getUserIdFromToken = (): string | null => {
    const token = localStorage.getItem('token')

    if (!token) return null

    try {
      const payloadBase64 = token.split('.')[1]
      const decodedPayload = JSON.parse(atob(payloadBase64))
      return decodedPayload.sub || null
    } catch (error) {
      console.error('Erreur de décodage du token', error)
      return null
    }
  }

  return {
    getUserIdFromToken
  }
}
