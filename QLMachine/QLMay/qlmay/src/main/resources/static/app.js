window.App = {
    async requireRole(roles) {
        const allowed = Array.isArray(roles) ? roles : [roles];
        document.body.style.visibility = 'hidden';
        let response;
        try {
            response = await fetch('/api/auth/me');
        } catch {
            window.location.href = '/login.html';
            throw new Error('Không thể kết nối tới máy chủ.');
        }
        if (!response.ok) {
            localStorage.removeItem('user');
            window.location.href = '/login.html';
            throw new Error('Phiên đăng nhập đã hết hạn.');
        }
        const user = await response.json();
        if (!allowed.includes(user.role)) {
            const destination = user.role === 'OWNER' ? '/owner.html'
                : user.role === 'OPERATOR' ? '/operator.html'
                    : user.role === 'ADMIN' ? '/admin.html' : '/index.html';
            if (typeof Swal !== 'undefined') {
                await Swal.fire({
                    icon: 'error',
                    title: 'Truy cập bị từ chối',
                    text: 'Tài khoản của bạn không có quyền truy cập trang này.',
                    confirmButtonText: 'Quay về trang của tôi',
                    allowOutsideClick: false
                });
            }
            window.location.href = destination;
            throw new Error('Bạn không có quyền truy cập trang này.');
        }
        localStorage.setItem('user', JSON.stringify(user));
        document.body.style.visibility = 'visible';
        return user;
    },

    async logout() {
        try {
            await fetch('/api/auth/logout', { method: 'POST' });
        } finally {
            localStorage.removeItem('user');
            window.location.href = '/login.html';
        }
    },

    async errorMessage(response, fallback) {
        try {
            const body = await response.json();
            return body.detail || body.message || body.error || fallback;
        } catch {
            return fallback;
        }
    },

    async getPendingHandover(bookingId, type) {
        const response = await fetch(`/api/bookings/${bookingId}/handovers`);
        if (!response.ok) throw new Error(await this.errorMessage(response, 'Không thể tải biên bản.'));
        const handovers = await response.json();
        const existing = handovers.find(item => item.type === type);
        if (!existing) return null;
        try {
            const metadata = JSON.parse(existing.remarks || '{}');
            if (!Array.isArray(metadata.signers)) return { completed: true, legacy: true };
            return metadata.signers.length === 1 ? existing : { completed: true };
        } catch {
            return { completed: true, legacy: true };
        }
    },

    signHandover(bookingId, handoverId) {
        return fetch(`/api/bookings/${bookingId}/handovers/${handoverId}/sign`, { method: 'POST' });
    },

    escape(value) {
        const element = document.createElement('span');
        element.textContent = value == null ? '' : String(value);
        return element.innerHTML;
    }
};
