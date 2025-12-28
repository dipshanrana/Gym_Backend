// Cross-Tab Authentication Synchronization Utility
// This file handles automatic login/logout across multiple browser tabs

class CrossTabAuth {
  constructor() {
    this.channel = null;
    this.storageKey = 'auth_event';
    this.initBroadcastChannel();
    this.initStorageListener();
  }

  /**
   * Initialize BroadcastChannel for modern browsers
   */
  initBroadcastChannel() {
    if ('BroadcastChannel' in window) {
      this.channel = new BroadcastChannel('auth_channel');

      this.channel.addEventListener('message', (event) => {
        this.handleAuthEvent(event.data);
      });
    }
  }

  /**
   * Initialize localStorage listener as fallback
   */
  initStorageListener() {
    window.addEventListener('storage', (event) => {
      if (event.key === this.storageKey && event.newValue) {
        try {
          const authEvent = JSON.parse(event.newValue);
          this.handleAuthEvent(authEvent);
        } catch (error) {
          console.error('Error parsing auth event:', error);
        }
      }
    });
  }

  /**
   * Handle authentication events from other tabs
   */
  handleAuthEvent(data) {
    console.log('Cross-tab auth event received:', data);

    switch (data.type) {
      case 'LOGIN':
        this.handleLogin(data.payload);
        break;
      case 'LOGOUT':
        this.handleLogout();
        break;
      case 'TOKEN_REFRESH':
        this.handleTokenRefresh(data.payload);
        break;
    }
  }

  /**
   * Handle login event from another tab
   */
  handleLogin(payload) {
    const { token, user } = payload;

    // Store authentication data
    localStorage.setItem('token', token);
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('userId', user.id);
      localStorage.setItem('username', user.username);
      localStorage.setItem('userRole', user.role);
    }

    console.log('✅ Auto-logged in from another tab');

    // Redirect to appropriate dashboard
    this.redirectToDashboard(user?.role);
  }

  /**
   * Handle logout event from another tab
   */
  handleLogout() {
    // Clear authentication data
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('userRole');

    console.log('✅ Auto-logged out from another tab');

    // Redirect to login page
    window.location.href = '/login';
  }

  /**
   * Handle token refresh from another tab
   */
  handleTokenRefresh(payload) {
    const { token } = payload;
    localStorage.setItem('token', token);
    console.log('✅ Token refreshed from another tab');
  }

  /**
   * Broadcast login event to other tabs
   */
  broadcastLogin(token, user) {
    const event = {
      type: 'LOGIN',
      payload: { token, user },
      timestamp: Date.now()
    };

    this.broadcast(event);
  }

  /**
   * Broadcast logout event to other tabs
   */
  broadcastLogout() {
    const event = {
      type: 'LOGOUT',
      timestamp: Date.now()
    };

    this.broadcast(event);
  }

  /**
   * Broadcast token refresh event to other tabs
   */
  broadcastTokenRefresh(token) {
    const event = {
      type: 'TOKEN_REFRESH',
      payload: { token },
      timestamp: Date.now()
    };

    this.broadcast(event);
  }

  /**
   * Broadcast event to all tabs
   */
  broadcast(event) {
    // Use BroadcastChannel if available
    if (this.channel) {
      this.channel.postMessage(event);
    }

    // Also use localStorage for compatibility
    localStorage.setItem(this.storageKey, JSON.stringify(event));

    // Clean up localStorage event after a short delay
    setTimeout(() => {
      localStorage.removeItem(this.storageKey);
    }, 1000);
  }

  /**
   * Redirect to appropriate dashboard based on role
   */
  redirectToDashboard(role) {
    // Only redirect if not already on a dashboard page
    const currentPath = window.location.pathname;

    if (currentPath === '/login' || currentPath === '/register' || currentPath === '/') {
      if (role === 'ROLE_ADMIN') {
        window.location.href = '/admin/dashboard';
      } else {
        window.location.href = '/dashboard';
      }
    } else {
      // Reload current page to update UI with logged-in state
      window.location.reload();
    }
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated() {
    return !!localStorage.getItem('token');
  }

  /**
   * Get current user data
   */
  getCurrentUser() {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }

  /**
   * Cleanup resources
   */
  destroy() {
    if (this.channel) {
      this.channel.close();
    }
  }
}

// Create singleton instance
const crossTabAuth = new CrossTabAuth();

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
  module.exports = crossTabAuth;
}

// Export for ES6 modules
export default crossTabAuth;

