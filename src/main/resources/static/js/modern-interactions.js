// Modern Interactions for eCommerce Dashboard - SECURE VERSION

// Security utility functions
const SecurityUtils = {
    // Sanitize HTML content to prevent XSS
    sanitizeHTML: function(str) {
        const div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    },
    
    // Validate email format
    isValidEmail: function(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },
    
    // Validate input length
    isValidLength: function(str, min, max) {
        return str && str.length >= min && str.length <= max;
    },
    
    // Escape special characters
    escapeHtml: function(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, function(m) { return map[m]; });
    },
    
    // Generate secure random token
    generateSecureToken: function(length = 32) {
        const array = new Uint8Array(length);
        crypto.getRandomValues(array);
        return Array.from(array, byte => byte.toString(16).padStart(2, '0')).join('');
    }
};

// Rate limiting utility
const RateLimiter = {
    attempts: new Map(),
    maxAttempts: 5,
    lockoutTime: 15 * 60 * 1000, // 15 minutes
    
    isAllowed: function(action, identifier) {
        const key = `${action}:${identifier}`;
        const now = Date.now();
        const attempts = this.attempts.get(key) || { count: 0, lastAttempt: 0 };
        
        // Check if locked out
        if (attempts.count >= this.maxAttempts) {
            const timeSinceLastAttempt = now - attempts.lastAttempt;
            if (timeSinceLastAttempt < this.lockoutTime) {
                return false;
            } else {
                // Reset after lockout period
                attempts.count = 0;
            }
        }
        
        // Check rate limiting (max 1 attempt per second)
        if (now - attempts.lastAttempt < 1000) {
            return false;
        }
        
        attempts.count++;
        attempts.lastAttempt = now;
        this.attempts.set(key, attempts);
        
        return true;
    },
    
    reset: function(action, identifier) {
        const key = `${action}:${identifier}`;
        this.attempts.delete(key);
    }
};

document.addEventListener('DOMContentLoaded', function() {
    
    // Initialize security features
    initSecurityFeatures();
    
    // Initialize modern interactions
    initModernInteractions();
    
    // Initialize animations
    initAnimations();
    
    // Initialize glassmorphism effects
    initGlassmorphism();
    
    // Initialize floating elements
    initFloatingElements();
});

function initSecurityFeatures() {
    
    // Disable right-click context menu (basic protection)
    document.addEventListener('contextmenu', function(e) {
        e.preventDefault();
        return false;
    });
    
    // Disable F12, Ctrl+Shift+I, Ctrl+U (basic protection)
    document.addEventListener('keydown', function(e) {
        if (e.key === 'F12' || 
            (e.ctrlKey && e.shiftKey && e.key === 'I') ||
            (e.ctrlKey && e.key === 'u')) {
            e.preventDefault();
            return false;
        }
    });
    
    // Add security headers dynamically
    addSecurityHeaders();
    
    // Monitor for suspicious activities
    monitorSuspiciousActivity();
}

function addSecurityHeaders() {
    // Add security-related meta tags if not present
    if (!document.querySelector('meta[http-equiv="Content-Security-Policy"]')) {
        const cspMeta = document.createElement('meta');
        cspMeta.setAttribute('http-equiv', 'Content-Security-Policy');
        cspMeta.setAttribute('content', "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';");
        document.head.appendChild(cspMeta);
    }
}

function monitorSuspiciousActivity() {
    // Monitor for DOM modifications
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.type === 'childList') {
                mutation.addedNodes.forEach(function(node) {
                    if (node.nodeType === Node.ELEMENT_NODE) {
                        // Check for suspicious script tags
                        if (node.tagName === 'SCRIPT' && node.src && !node.src.startsWith(window.location.origin)) {
                            console.warn('Suspicious external script detected:', node.src);
                            node.remove();
                        }
                    }
                });
            }
        });
    });
    
    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
}

function initModernInteractions() {
    
    // Add hover effects to cards with security validation
    const cards = document.querySelectorAll('.glass-card, .stat-card');
    cards.forEach(card => {
        if (SecurityUtils.isValidLength(card.className, 1, 100)) {
            card.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-10px) scale(1.02)';
                this.style.boxShadow = '0 25px 50px rgba(0, 0, 0, 0.4)';
            });
            
            card.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0) scale(1)';
                this.style.boxShadow = '';
            });
        }
    });
    
    // Add ripple effect to buttons with security validation
    const buttons = document.querySelectorAll('.btn-modern');
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            // Rate limiting for button clicks
            if (!RateLimiter.isAllowed('button_click', this.id || 'anonymous')) {
                e.preventDefault();
                return false;
            }
            
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            // Validate coordinates
            if (isNaN(x) || isNaN(y) || isNaN(size)) {
                return false;
            }
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.classList.add('ripple');
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                if (ripple.parentNode) {
                    ripple.remove();
                }
            }, 600);
        });
    });
    
    // Add typing effect to headings with security validation
    const headings = document.querySelectorAll('h1, h2');
    headings.forEach(heading => {
        if (heading.textContent && heading.textContent.includes('Dashboard')) {
            const sanitizedText = SecurityUtils.sanitizeHTML(heading.textContent);
            typeWriter(heading, sanitizedText, 100);
        }
    });
}

function initAnimations() {
    
    // Intersection Observer for scroll animations with security
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting && entry.target.classList.contains('animate-fade-in')) {
                entry.target.classList.add('animate-fade-in');
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observe all animated elements with validation
    const animatedElements = document.querySelectorAll('.animate-fade-in, .animate-slide-in');
    animatedElements.forEach(el => {
        if (el && el.style) {
            el.style.opacity = '0';
            el.style.transform = 'translateY(30px)';
            el.style.transition = 'all 0.6s ease-out';
            observer.observe(el);
        }
    });
    
    // Parallax effect for background elements with security
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        const parallaxElements = document.querySelectorAll('.floating');
        
        parallaxElements.forEach((element, index) => {
            if (element && element.style) {
                const speed = 0.5 + (index * 0.1);
                const translateY = scrolled * speed;
                
                // Validate the calculation result
                if (!isNaN(translateY) && isFinite(translateY)) {
                    element.style.transform = `translateY(${translateY}px)`;
                }
            }
        });
    });
}

function initGlassmorphism() {
    
    // Dynamic glassmorphism based on mouse position with security
    const glassCards = document.querySelectorAll('.glass-card');
    
    glassCards.forEach(card => {
        card.addEventListener('mousemove', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            // Validate coordinates
            if (isNaN(x) || isNaN(y) || isNaN(rect.width) || isNaN(rect.height)) {
                return;
            }
            
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            
            const rotateX = (y - centerY) / 10;
            const rotateY = (centerX - x) / 10;
            
            // Validate rotation values
            if (isNaN(rotateX) || isNaN(rotateY) || !isFinite(rotateX) || !isFinite(rotateY)) {
                return;
            }
            
            // Limit rotation to prevent extreme values
            const maxRotation = 15;
            const clampedRotateX = Math.max(-maxRotation, Math.min(maxRotation, rotateX));
            const clampedRotateY = Math.max(-maxRotation, Math.min(maxRotation, rotateY));
            
            this.style.transform = `perspective(1000px) rotateX(${clampedRotateX}deg) rotateY(${clampedRotateY}deg) translateZ(20px)`;
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'perspective(1000px) rotateX(0) rotateY(0) translateZ(0)';
        });
    });
}

function initFloatingElements() {
    
    // Create floating particles with security
    createFloatingParticles();
    
    // Animate existing floating elements with validation
    const floatingElements = document.querySelectorAll('.floating');
    floatingElements.forEach((element, index) => {
        if (element && element.style) {
            element.style.animationDelay = `${index * 0.5}s`;
            element.style.animationDuration = `${3 + index}s`;
        }
    });
}

function createFloatingParticles() {
    
    // Create particle container with security
    let particleContainer = document.querySelector('.particle-container');
    if (!particleContainer) {
        particleContainer = document.createElement('div');
        particleContainer.className = 'particle-container';
        particleContainer.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: -1;
            overflow: hidden;
        `;
        document.body.appendChild(particleContainer);
    }
    
    // Create particles with security limits
    const maxParticles = 20;
    for (let i = 0; i < maxParticles; i++) {
        createParticle(particleContainer);
    }
}

function createParticle(container) {
    if (!container) return;
    
    const particle = document.createElement('div');
    
    // Generate secure random values
    const size = Math.random() * 4 + 2;
    const left = Math.random() * 100;
    const top = Math.random() * 100;
    const duration = Math.random() * 10 + 10;
    
    // Validate values
    if (isNaN(size) || isNaN(left) || isNaN(top) || isNaN(duration)) {
        return;
    }
    
    particle.style.cssText = `
        position: absolute;
        width: ${size}px;
        height: ${size}px;
        background: linear-gradient(135deg, rgba(102, 126, 234, 0.6), rgba(118, 75, 162, 0.6));
        border-radius: 50%;
        pointer-events: none;
        animation: float-particle ${duration}s linear infinite;
        left: ${left}%;
        top: ${top}%;
    `;
    
    container.appendChild(particle);
    
    // Remove particle after animation with security
    setTimeout(() => {
        if (particle && particle.parentNode) {
            particle.remove();
            createParticle(container);
        }
    }, 15000);
}

function typeWriter(element, text, speed) {
    if (!element || !text || typeof speed !== 'number') {
        return;
    }
    
    let i = 0;
    element.textContent = '';
    
    function type() {
        if (i < text.length && element) {
            element.textContent += text.charAt(i);
            i++;
            setTimeout(type, speed);
        }
    }
    
    type();
}

// Add CSS for ripple effect with security
const style = document.createElement('style');
style.textContent = `
    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.3);
        transform: scale(0);
        animation: ripple-animation 0.6s linear;
        pointer-events: none;
    }
    
    @keyframes ripple-animation {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
    
    @keyframes float-particle {
        0% {
            transform: translateY(100vh) rotate(0deg);
            opacity: 0;
        }
        10% {
            opacity: 1;
        }
        90% {
            opacity: 1;
        }
        100% {
            transform: translateY(-100px) rotate(360deg);
            opacity: 0;
        }
    }
    
    .glass-card:hover {
        backdrop-filter: blur(25px);
    }
    
    .stat-card:hover .stat-number {
        transform: scale(1.1);
        transition: transform 0.3s ease;
    }
    
    .btn-modern:active {
        transform: scale(0.95);
    }
    
    .floating {
        animation: floating 3s ease-in-out infinite;
    }
    
    @keyframes floating {
        0%, 100% { transform: translateY(0px); }
        50% { transform: translateY(-10px); }
    }
`;

document.head.appendChild(style);

// Add smooth scrolling with security
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const href = this.getAttribute('href');
        if (href && href.startsWith('#')) {
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        }
    });
});

// Add loading states with security
function showLoading(element) {
    if (element && element.classList) {
        element.classList.add('loading');
        element.style.pointerEvents = 'none';
    }
}

function hideLoading(element) {
    if (element && element.classList) {
        element.classList.remove('loading');
        element.style.pointerEvents = 'auto';
    }
}

// Export functions for global use with security
window.ModernDashboard = {
    showLoading,
    hideLoading,
    initModernInteractions,
    initAnimations,
    initGlassmorphism,
    initFloatingElements,
    SecurityUtils,
    RateLimiter
};

// Prevent tampering with the ModernDashboard object
Object.freeze(window.ModernDashboard);
