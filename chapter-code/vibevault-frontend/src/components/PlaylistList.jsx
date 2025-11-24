import { useEffect, useState } from 'react'

const PlaylistList = () => {
  const [playlists, setPlaylists] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    const controller = new AbortController()

    const fetchPlaylists = async () => {
      setLoading(true)
      setError(null)

      try {
        const response = await fetch('http://localhost:8080/api/playlists', {
          signal: controller.signal,
        })

        if (!response.ok) {
          throw new Error(`服务器返回 ${response.status}`)
        }

        const data = await response.json()
        setPlaylists(data)
      } catch (err) {
        if (err.name !== 'AbortError') {
          setError(err.message)
        }
      } finally {
        setLoading(false)
      }
    }

    fetchPlaylists()

    return () => {
      controller.abort()
    }
  }, [])

  if (loading) {
    return <p>加载中…</p>
  }

  if (error) {
    return <p style={{ color: 'red' }}>加载失败：{error}</p>
  }

  return (
    <ul>
      {playlists.map((playlist) => (
        <li key={playlist.id}>{playlist.name}</li>
      ))}
    </ul>
  )
}

export default PlaylistList

