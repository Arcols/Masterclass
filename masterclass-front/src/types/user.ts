export interface UserProfileData {
  firstName?: string
  lastName?: string
  email?: string
  description?: string
  groups?: string[]
}

export interface BackendUserResponse {
  useFirstname: string
  useLastname: string
  useMail: string
  useDescription: string
  groups: { groId: string }[]
}
