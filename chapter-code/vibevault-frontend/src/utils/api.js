const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

const defaultHeaders = {
  'Content-Type': 'application/json',
};

async function handleResponse(response) {
  if (response.status === 401 || response.status === 403) {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    window.location.reload();
    throw new Error('未授权，请重新登录');
  }

  if (!response.ok) {
    const payload = await response.json().catch(() => ({}));
    throw new Error(payload.message || `请求失败：${response.status}`);
  }

  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

export async function apiGet(endpoint) {
  const token = localStorage.getItem('jwtToken');
  const headers = {
    ...defaultHeaders,
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
  const response = await fetch(`${API_BASE}/api${endpoint}`, {
    method: 'GET',
    headers,
  });
  return handleResponse(response);
}

export async function apiPost(endpoint, body) {
  const token = localStorage.getItem('jwtToken');
  const headers = {
    ...defaultHeaders,
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
  const response = await fetch(`${API_BASE}/api${endpoint}`, {
    method: 'POST',
    headers,
    body: JSON.stringify(body),
  });
  return handleResponse(response);
}

export async function apiDelete(endpoint) {
  const token = localStorage.getItem('jwtToken');
  const headers = {
    ...defaultHeaders,
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
  const response = await fetch(`${API_BASE}/api${endpoint}`, {
    method: 'DELETE',
    headers,
  });
  return handleResponse(response);
}

