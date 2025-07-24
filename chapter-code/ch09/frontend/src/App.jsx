import { useState, useEffect } from 'react';

function App() {
  // 1. 定义三个状态，覆盖所有UI场景
  const [playlists, setPlaylists] = useState([]); // 初始为空数组
  const [loading, setLoading] = useState(true);   // 初始为true，因为一开始就要加载
  const [error, setError] = useState(null);       // 初始为null，因为还没有错误

  // 2. 使用useEffect来执行获取数据的副作用
  useEffect(() => {
    // 定义一个异步函数来获取数据，这样我们就可以在内部使用await
    async function fetchPlaylists() {
      try {
        // 后端API的地址
        const apiUrl = 'http://localhost:8080/api/playlists';
        const response = await fetch(apiUrl);

        // 如果响应状态码不是2xx (e.g., 404, 500)，则它不是一个成功的请求
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        setPlaylists(data); // 成功，用获取的数据更新状态
        setError(null);     // 清除之前的错误
      } catch (err) {
        // 如果在fetch或解析过程中发生任何错误
        setError(err.message); // 将错误信息存入状态
        setPlaylists([]);      // 清空数据
      } finally {
        // 无论成功还是失败，这个代码块都会执行
        setLoading(false); // 加载过程结束
      }
    }

    fetchPlaylists(); // 调用我们定义的异步函数

  }, []); // <-- 注意这个空的依赖数组！

  // 3. 根据状态，进行条件渲染
  
  // 场景一：正在加载
  if (loading) {
    return <div>Loading playlists...</div>;
  }

  // 场景二：发生错误
  if (error) {
    return <div style={{ color: 'red' }}>Error: {error}</div>;
  }

  // 场景三：成功获取数据
  return (
    <div>
      <h1>My Vibe Vault Playlists</h1>
      <ul>
        {playlists.map(playlist => (
          <li key={playlist.name}>
            {playlist.name}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;