export default (response) => {
  if (!response.ok) {
    throw response
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}
