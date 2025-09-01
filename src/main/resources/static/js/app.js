/**
 * BIBLIO-TECH - APPLICATION JAVASCRIPT PRINCIPAL
 * ==============================================
 */

// Configuration globale
const APP_CONFIG = {
    name: 'Biblio-Tech',
    version: '1.0.0',
    apiBaseUrl: '/api',
    dateFormat: 'dd/MM/yyyy',
    currency: 'EUR',
    language: 'fr'
};

// Classe principale de l'application
class BiblioTechApp {
    constructor() {
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.initializeComponents();
        this.setupGlobalHandlers();
        console.log(`${APP_CONFIG.name} v${APP_CONFIG.version} initialisé`);
    }

    setupEventListeners() {
        // Gestion des formulaires
        document.addEventListener('submit', this.handleFormSubmit.bind(this));
        
        // Gestion des clics sur les boutons de suppression
        document.addEventListener('click', this.handleDeleteButtons.bind(this));
        
        // Gestion des modales
        document.addEventListener('click', this.handleModalTriggers.bind(this));
        
        // Gestion des tooltips et popovers Bootstrap
        document.addEventListener('DOMContentLoaded', this.initializeBootstrapComponents.bind(this));
    }

    initializeComponents() {
        // Initialisation des composants Bootstrap
        this.initializeBootstrapComponents();
        
        // Initialisation des composants personnalisés
        this.initializeCustomComponents();
        
        // Configuration des notifications
        this.setupNotifications();
    }

    initializeBootstrapComponents() {
        // Initialisation des tooltips
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });

        // Initialisation des popovers
        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl);
        });

        // Initialisation des modales
        const modalTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="modal"]'));
        modalTriggerList.map(function (modalTriggerEl) {
            return new bootstrap.Modal(modalTriggerEl);
        });
    }

    initializeCustomComponents() {
        // Initialisation des composants personnalisés
        this.initializeDataTables();
        this.initializeDatePickers();
        this.initializeSelect2();
        this.initializeCharts();
    }

    setupGlobalHandlers() {
        // Gestion des erreurs globales
        window.addEventListener('error', this.handleGlobalError.bind(this));
        
        // Gestion des erreurs de promesses non gérées
        window.addEventListener('unhandledrejection', this.handleUnhandledRejection.bind(this));
        
        // Gestion de la navigation
        window.addEventListener('beforeunload', this.handleBeforeUnload.bind(this));
    }

    // Gestion des formulaires
    handleFormSubmit(event) {
        const form = event.target;
        
        // Validation côté client
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
            form.classList.add('was-validated');
            return false;
        }

        // Ajout de l'indicateur de chargement
        this.showFormLoading(form);
        
        // Validation des champs personnalisés
        if (!this.validateCustomFields(form)) {
            event.preventDefault();
            this.hideFormLoading(form);
            return false;
        }
    }

    // Validation des champs personnalisés
    validateCustomFields(form) {
        let isValid = true;
        
        // Validation des dates
        const dateFields = form.querySelectorAll('input[type="date"]');
        dateFields.forEach(field => {
            if (field.value && !this.isValidDate(field.value)) {
                this.showFieldError(field, 'Date invalide');
                isValid = false;
            }
        });

        // Validation des nombres
        const numberFields = form.querySelectorAll('input[type="number"]');
        numberFields.forEach(field => {
            if (field.value && !this.isValidNumber(field.value)) {
                this.showFieldError(field, 'Nombre invalide');
                isValid = false;
            }
        });

        // Validation des emails
        const emailFields = form.querySelectorAll('input[type="email"]');
        emailFields.forEach(field => {
            if (field.value && !this.isValidEmail(field.value)) {
                this.showFieldError(field, 'Email invalide');
                isValid = false;
            }
        });

        return isValid;
    }

    // Gestion des boutons de suppression
    handleDeleteButtons(event) {
        if (event.target.matches('[data-action="delete"]') || 
            event.target.closest('[data-action="delete"]')) {
            event.preventDefault();
            this.confirmDelete(event.target);
        }
    }

    // Confirmation de suppression
    confirmDelete(element) {
        const itemName = element.dataset.itemName || 'cet élément';
        const itemId = element.dataset.itemId;
        const deleteUrl = element.dataset.deleteUrl || element.href;
        
        if (confirm(`Êtes-vous sûr de vouloir supprimer ${itemName} ? Cette action est irréversible.`)) {
            this.performDelete(deleteUrl, itemId);
        }
    }

    // Exécution de la suppression
    async performDelete(url, itemId) {
        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            });

            if (response.ok) {
                this.showNotification('Élément supprimé avec succès', 'success');
                this.removeItemFromDOM(itemId);
            } else {
                throw new Error('Erreur lors de la suppression');
            }
        } catch (error) {
            console.error('Erreur de suppression:', error);
            this.showNotification('Erreur lors de la suppression', 'error');
        }
    }

    // Gestion des modales
    handleModalTriggers(event) {
        if (event.target.matches('[data-bs-toggle="modal"]')) {
            const targetModal = event.target.dataset.bsTarget;
            if (targetModal) {
                this.openModal(targetModal);
            }
        }
    }

    // Ouverture de modale
    openModal(modalId) {
        const modal = document.querySelector(modalId);
        if (modal) {
            const bootstrapModal = new bootstrap.Modal(modal);
            bootstrapModal.show();
        }
    }

    // Initialisation des DataTables
    initializeDataTables() {
        const tables = document.querySelectorAll('.datatable');
        tables.forEach(table => {
            if (typeof $.fn.DataTable !== 'undefined') {
                $(table).DataTable({
                    language: {
                        url: '/js/datatables-fr.json'
                    },
                    responsive: true,
                    pageLength: 25,
                    order: [[0, 'desc']],
                    dom: '<"row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>' +
                         '<"row"<"col-sm-12"tr>>' +
                         '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
                    buttons: ['copy', 'excel', 'pdf', 'print']
                });
            }
        });
    }

    // Initialisation des DatePickers
    initializeDatePickers() {
        const dateFields = document.querySelectorAll('input[type="date"], .datepicker');
        dateFields.forEach(field => {
            if (typeof flatpickr !== 'undefined') {
                flatpickr(field, {
                    dateFormat: APP_CONFIG.dateFormat,
                    locale: 'fr',
                    allowInput: true,
                    altInput: true,
                    altFormat: 'd/m/Y'
                });
            }
        });
    }

    // Initialisation de Select2
    initializeSelect2() {
        const selectFields = document.querySelectorAll('select.select2');
        selectFields.forEach(field => {
            if (typeof $ !== 'undefined' && $.fn.select2) {
                $(field).select2({
                    theme: 'bootstrap-5',
                    width: '100%',
                    placeholder: 'Sélectionnez une option...',
                    allowClear: true
                });
            }
        });
    }

    // Initialisation des graphiques
    initializeCharts() {
        // Les graphiques sont initialisés dans les pages spécifiques
        // car ils nécessitent des données spécifiques
    }

    // Gestion des notifications
    setupNotifications() {
        // Configuration des notifications toast
        if (typeof Toastify !== 'undefined') {
            Toastify.defaults = {
                duration: 5000,
                gravity: 'top',
                position: 'right',
                stopOnFocus: true
            };
        }
    }

    // Affichage de notification
    showNotification(message, type = 'info') {
        if (typeof Toastify !== 'undefined') {
            Toastify({
                text: message,
                className: `toast-${type}`,
                style: {
                    background: this.getNotificationColor(type)
                }
            }).showToast();
        } else {
            // Fallback vers alert standard
            alert(message);
        }
    }

    // Couleurs des notifications
    getNotificationColor(type) {
        const colors = {
            success: '#059669',
            error: '#DC2626',
            warning: '#F59E0B',
            info: '#3B82F6'
        };
        return colors[type] || colors.info;
    }

    // Gestion du chargement des formulaires
    showFormLoading(form) {
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Chargement...';
        }
    }

    hideFormLoading(form) {
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = submitBtn.dataset.originalText || 'Enregistrer';
        }
    }

    // Utilitaires de validation
    isValidDate(dateString) {
        const date = new Date(dateString);
        return date instanceof Date && !isNaN(date);
    }

    isValidNumber(value) {
        return !isNaN(value) && isFinite(value);
    }

    isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // Affichage des erreurs de champ
    showFieldError(field, message) {
        const errorDiv = field.parentNode.querySelector('.invalid-feedback');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
        }
        field.classList.add('is-invalid');
    }

    // Suppression d'élément du DOM
    removeItemFromDOM(itemId) {
        const element = document.querySelector(`[data-item-id="${itemId}"]`);
        if (element) {
            element.closest('tr')?.remove() || element.remove();
        }
    }

    // Gestion des erreurs globales
    handleGlobalError(error) {
        console.error('Erreur globale:', error);
        this.showNotification('Une erreur inattendue s\'est produite', 'error');
    }

    // Gestion des promesses non gérées
    handleUnhandledRejection(event) {
        console.error('Promesse rejetée non gérée:', event.reason);
        this.showNotification('Une erreur s\'est produite lors du traitement', 'error');
    }

    // Gestion avant déchargement de la page
    handleBeforeUnload(event) {
        // Vérifier s'il y a des formulaires non sauvegardés
        const forms = document.querySelectorAll('form');
        let hasUnsavedChanges = false;
        
        forms.forEach(form => {
            if (form.dataset.hasChanges === 'true') {
                hasUnsavedChanges = true;
            }
        });

        if (hasUnsavedChanges) {
            event.preventDefault();
            event.returnValue = 'Vous avez des modifications non sauvegardées. Voulez-vous vraiment quitter ?';
            return event.returnValue;
        }
    }

    // Méthodes utilitaires
    formatDate(date, format = APP_CONFIG.dateFormat) {
        if (!date) return '';
        
        const d = new Date(date);
        if (isNaN(d.getTime())) return '';
        
        const day = String(d.getDate()).padStart(2, '0');
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const year = d.getFullYear();
        
        return format.replace('dd', day).replace('MM', month).replace('yyyy', year);
    }

    formatCurrency(amount, currency = APP_CONFIG.currency) {
        return new Intl.NumberFormat('fr-FR', {
            style: 'currency',
            currency: currency
        }).format(amount);
    }

    // Méthode pour détecter les changements dans les formulaires
    detectFormChanges() {
        const forms = document.querySelectorAll('form');
        forms.forEach(form => {
            const inputs = form.querySelectorAll('input, select, textarea');
            inputs.forEach(input => {
                input.addEventListener('change', () => {
                    form.dataset.hasChanges = 'true';
                });
                input.addEventListener('input', () => {
                    form.dataset.hasChanges = 'true';
                });
            });
        });
    }
}

// Initialisation de l'application quand le DOM est prêt
document.addEventListener('DOMContentLoaded', () => {
    window.biblioTechApp = new BiblioTechApp();
});

// Export pour utilisation dans d'autres modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = BiblioTechApp;
}
