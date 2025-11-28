import { useState, useEffect, useCallback } from 'react';
import { 
  Home, Search, Library, Plus, ChevronLeft, ChevronRight, 
  Play, Pause, SkipBack, SkipForward, Repeat, Shuffle, 
  Mic2, Layers, Volume2, Trash2, Music, Disc 
} from 'lucide-react';
import Login from './Login';
import './App.css';
import { apiGet, apiPost, apiDelete } from './utils/api';

function App() {
  // --- State ---
  const [view, setView] = useState({ type: 'home' });
  const [playlists, setPlaylists] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newPlaylistName, setNewPlaylistName] = useState('');
  const [showAddSongModal, setShowAddSongModal] = useState(false);
  const [songForm, setSongForm] = useState({ title: '', artist: '' });
  const [error, setError] = useState('');
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('jwtToken'));
  const [currentUser, setCurrentUser] = useState(localStorage.getItem('username') ?? '');
  
  // Player Mock State
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTrack, setCurrentTrack] = useState(null);

  // --- API ---
  const loadPlaylists = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const data = await apiGet('/playlists');
      setPlaylists(data);
    } catch (e) {
      console.error("Failed to load playlists", e);
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (isAuthenticated) {
      loadPlaylists();
    } else {
      setPlaylists([]);
    }
  }, [isAuthenticated, loadPlaylists]);

  const handleLoginSuccess = (username) => {
    setIsAuthenticated(true);
    setCurrentUser(username);
    localStorage.setItem('username', username);
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    setIsAuthenticated(false);
    setCurrentUser('');
    setPlaylists([]);
    setView({ type: 'home' });
  };

  if (!isAuthenticated) {
    return <Login onLoginSuccess={handleLoginSuccess} />;
  }

  if (loading) {
    return <div className="loading-screen">加载中...</div>;
  }

  const handleCreatePlaylist = async (e) => {
    e.preventDefault();
    if (!newPlaylistName.trim()) return;
    try {
      await apiPost('/playlists', { name: newPlaylistName });
      await loadPlaylists();
      setShowCreateModal(false);
      setNewPlaylistName('');
    } catch (err) {
      console.error(err);
      alert('Failed to create playlist');
    }
  };

  const handleDeletePlaylist = async (id, e) => {
    e.stopPropagation();
    if (!confirm('Are you sure you want to delete this playlist?')) return;
    try {
      await apiDelete(`/playlists/${id}`);
      await loadPlaylists();
      if (view.type === 'playlist' && view.id === id) {
        setView({ type: 'home' });
      }
    } catch (err) {
      console.error(err);
      alert('Failed to delete');
    }
  };

  const handleAddSong = async (e) => {
    e.preventDefault();
    if (!songForm.title || !songForm.artist) return;
    if (view.type !== 'playlist') return;

    try {
      await apiPost(`/playlists/${view.id}/songs`, songForm);
      await loadPlaylists();
      setShowAddSongModal(false);
      setSongForm({ title: '', artist: '' });
    } catch (err) {
      console.error(err);
      alert('Failed to add song');
    }
  };

  const handleRemoveSong = async (playlistId, songId) => {
    try {
      await apiDelete(`/playlists/${playlistId}/songs/${songId}`);
      await loadPlaylists();
    } catch (err) {
      console.error(err);
      alert('Failed to remove song');
    }
  };

  // --- Helpers ---
  const activePlaylist = view.type === 'playlist' 
    ? playlists.find(p => p.id === view.id) 
    : null;

  const playTrack = (track) => {
    setCurrentTrack(track);
    setIsPlaying(true);
  };

  // --- Render Components ---

  return (
    <div className="app-container">
      {error && <div className="error-banner">{error}</div>}
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-nav">
          <div 
            className={`nav-item ${view.type === 'home' ? 'active' : ''}`}
            onClick={() => setView({ type: 'home' })}
          >
            <Home size={24} />
            <span>Home</span>
          </div>
          <div className="nav-item">
            <Search size={24} />
            <span>Search</span>
          </div>
        </div>

        <div className="sidebar-library">
          <div className="library-header">
            <h3><Library size={24} /> Your Library</h3>
            <button className="icon-btn" onClick={() => setShowCreateModal(true)}>
              <Plus size={20} />
            </button>
          </div>
          
          <div className="library-list">
            {playlists.map(pl => (
              <div 
                key={pl.id} 
                className={`library-item ${view.type === 'playlist' && view.id === pl.id ? 'active' : ''}`}
                onClick={() => setView({ type: 'playlist', id: pl.id })}
              >
                <div className="library-item-img">
                  <Music size={20} />
                </div>
                <div className="library-item-info">
                  <span className="library-item-name">{pl.name}</span>
                  <span className="library-item-sub">Playlist • {pl.songs.length} songs</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="main-view">
        <div className="top-bar">
          <div className="nav-buttons">
            <button className="circle-btn" onClick={() => setView({ type: 'home' })}>
              <ChevronLeft size={22} />
            </button>
            <button className="circle-btn">
              <ChevronRight size={22} />
            </button>
          </div>
          <div className="user-actions">
            <button className="pill-btn">Explore Premium</button>
            <div className="user-info">
              <span>欢迎, {currentUser || 'VibeFriend'}</span>
              <button className="logout-btn" onClick={handleLogout}>
                退出登录
              </button>
            </div>
          </div>
        </div>

        <div className="content-spacing">
          {view.type === 'home' ? (
            <>
              <h2 className="section-title">Your Playlists</h2>
              <div className="cards-grid">
                {playlists.map(pl => (
                  <div 
                    key={pl.id} 
                    className="playlist-card"
                    onClick={() => setView({ type: 'playlist', id: pl.id })}
                  >
                    <div className="card-img">
                      <Music size={48} />
                      <div className="play-btn-hover">
                        <Play size={24} fill="black" />
                      </div>
                    </div>
                    <div className="card-title">{pl.name}</div>
                    <div className="card-desc">By VibeVault User</div>
                  </div>
                ))}
              </div>
            </>
          ) : activePlaylist ? (
            <>
              <div className="playlist-header">
                <div className="playlist-cover-large">
                  <Music size={64} />
                </div>
                <div className="playlist-info">
                  <span style={{fontSize: '0.8rem', fontWeight: 700}}>Playlist</span>
                  <h1>{activePlaylist.name}</h1>
                  <div className="playlist-meta">
                    <span>VibeVault User</span>
                    <span>•</span>
                    <span>{activePlaylist.songs.length} songs</span>
                  </div>
                </div>
              </div>

              <div className="action-bar">
                <div className="play-btn-large" onClick={() => activePlaylist.songs[0] && playTrack(activePlaylist.songs[0])}>
                   {isPlaying && currentTrack && activePlaylist.songs.find(s => s.id === currentTrack.id) ? <Pause size={28} fill="black" /> : <Play size={28} fill="black" />}
                </div>
                <button className="icon-btn" style={{fontSize: 32}} onClick={() => setShowAddSongModal(true)}>
                  <Plus size={32} />
                </button>
                <button className="icon-btn" onClick={(e) => handleDeletePlaylist(activePlaylist.id, e)}>
                  <Trash2 size={32} />
                </button>
              </div>

              <div className="track-list">
                <div className="track-list-header">
                  <span>#</span>
                  <span>Title</span>
                  <span>Artist</span>
                  <span><Volume2 size={16} /></span>
                </div>
                {activePlaylist.songs.map((song, idx) => (
                  <div key={song.id} className="track-row" onDoubleClick={() => playTrack(song)}>
                    <span className="track-num">{idx + 1}</span>
                    <span className="track-play-icon" onClick={() => playTrack(song)}><Play size={12} fill="white" /></span>
                    
                    <div className="track-title">{song.title}</div>
                    <div className="track-artist">{song.artist}</div>
                    <button 
                      className="icon-btn" 
                      style={{opacity: 0.5}}
                      onClick={() => handleRemoveSong(activePlaylist.id, song.id)}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
                {activePlaylist.songs.length === 0 && (
                   <div style={{padding: 20, textAlign: 'center', color: '#b3b3b3'}}>
                     This playlist is empty. Click + to add songs.
                   </div>
                )}
              </div>
            </>
          ) : (
            <div>Playlist not found</div>
          )}
        </div>
      </main>

      {/* Player Bar */}
      <footer className="player-bar">
        <div className="now-playing">
          {currentTrack && (
            <>
              <div className="np-cover">
                <div style={{width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#333'}}>
                  <Disc size={24} />
                </div>
              </div>
              <div className="np-info">
                <div className="np-title">{currentTrack.title}</div>
                <div className="np-artist">{currentTrack.artist}</div>
              </div>
            </>
          )}
        </div>

        <div className="player-controls">
          <div className="control-buttons">
            <button className="control-btn"><Shuffle size={16} /></button>
            <button className="control-btn"><SkipBack size={20} fill="currentColor" /></button>
            <button 
              className="control-btn play"
              onClick={() => setIsPlaying(!isPlaying)}
            >
              {isPlaying ? <Pause size={20} fill="black" /> : <Play size={20} fill="black" />}
            </button>
            <button className="control-btn"><SkipForward size={20} fill="currentColor" /></button>
            <button className="control-btn"><Repeat size={16} /></button>
          </div>
          <div className="progress-bar">
            <div className="progress-val" style={{width: isPlaying ? '45%' : '0%'}}></div>
          </div>
        </div>

        <div className="volume-controls">
          <Mic2 size={16} color="#b3b3b3" />
          <Layers size={16} color="#b3b3b3" />
          <Volume2 size={16} color="#b3b3b3" />
          <div style={{width: 80, height: 4, background: '#535353', borderRadius: 2}}>
            <div style={{width: '70%', height: '100%', background: 'white'}}></div>
          </div>
        </div>
      </footer>

      {/* Modals */}
      {showCreateModal && (
        <div className="modal-overlay" onClick={() => setShowCreateModal(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <h2 style={{color: 'white', margin: 0}}>Create Playlist</h2>
            <form onSubmit={handleCreatePlaylist}>
              <input 
                autoFocus
                className="spotify-input"
                placeholder="Playlist Name"
                value={newPlaylistName}
                onChange={e => setNewPlaylistName(e.target.value)} 
              />
              <div style={{display: 'flex', justifyContent: 'flex-end', marginTop: 16}}>
                <button type="submit" className="pill-btn">Create</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showAddSongModal && (
        <div className="modal-overlay" onClick={() => setShowAddSongModal(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <h2 style={{color: 'white', margin: 0}}>Add Song to {activePlaylist?.name}</h2>
            <form onSubmit={handleAddSong} style={{display: 'flex', flexDirection: 'column', gap: 12}}>
              <input 
                autoFocus
                className="spotify-input"
                placeholder="Title"
                value={songForm.title}
                onChange={e => setSongForm({...songForm, title: e.target.value})} 
              />
              <input 
                className="spotify-input"
                placeholder="Artist"
                value={songForm.artist}
                onChange={e => setSongForm({...songForm, artist: e.target.value})} 
              />
              <div style={{display: 'flex', justifyContent: 'flex-end', marginTop: 16}}>
                <button type="submit" className="pill-btn">Add</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
