// Replaces PokemonCardView#createPlaceholderImage — swaps a missing <img> for a CSS circle
window.pokemonImageFallback = function (imgEl) {
    const wrapper = imgEl.parentElement;
    const color = imgEl.getAttribute('data-color') || 'linear-gradient(to bottom, #F0F0F0, #D0D0D0)';
    const initial = imgEl.getAttribute('data-initial') || '?';

    const placeholder = document.createElement('div');
    placeholder.className = 'pokemon-image-placeholder';
    placeholder.style.background = color;
    placeholder.textContent = 'No Image';

    wrapper.replaceChild(placeholder, imgEl);
};

// Replaces the JavaFX Timeline-driven slideshow (3-second KeyFrame cycle)
document.addEventListener('DOMContentLoaded', function () {
    const slideshowBtn = document.getElementById('slideshowBtn');
    const cardPanel = document.getElementById('cardPanel');
    const deck = document.getElementById('slideshowDeck');
    const slides = deck ? Array.from(deck.querySelectorAll('.slide')) : [];

    let intervalId = null;
    let currentIndex = 0;
    let restoreEl = null; // whatever was visible before the slideshow started

    function showSlide(index) {
        deck.style.display = 'block';
        slides.forEach((slide, i) => { slide.style.display = (i === index) ? 'block' : 'none'; });
    }

    function stopSlideshow() {
        if (intervalId) clearInterval(intervalId);
        intervalId = null;
        deck.style.display = 'none';
        slideshowBtn.textContent = 'Start Slideshow';
        if (restoreEl) restoreEl.style.display = '';
    }

    if (slideshowBtn && slides.length > 0) {
        slideshowBtn.addEventListener('click', function () {
            if (intervalId) {
                stopSlideshow();
                return;
            }

            // Hide whatever the card panel is currently showing (welcome/message/selected card)
            restoreEl = null;
            Array.from(cardPanel.children).forEach(child => {
                if (child.id !== 'slideshowDeck') {
                    if (child.style.display !== 'none') restoreEl = child;
                    child.style.display = 'none';
                }
            });

            slideshowBtn.textContent = 'Stop Slideshow';
            currentIndex = 0;
            showSlide(currentIndex);

            intervalId = setInterval(function () {
                currentIndex++;
                if (currentIndex >= slides.length) {
                    stopSlideshow();
                } else {
                    showSlide(currentIndex);
                }
            }, 3000); // matches Duration.seconds(3) from the original Timeline
        });
    } else if (slideshowBtn) {
        slideshowBtn.disabled = true;
        slideshowBtn.title = 'No Pokemon available for slideshow.';
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const musicManager = BackgroundMusicManager.getInstance();
    musicManager.loadMusic('pokemon_theme', '/audio/pokemon_theme.mp3');
    musicManager.setVolume(0.5);

    const musicBtn = document.getElementById('musicToggleBtn');
    if (musicBtn) {
        musicBtn.addEventListener('click', async function () {
            if (musicManager.isPlaying()) {
                musicManager.pauseMusic();
                musicBtn.textContent = ' Play Music';
            } else {
                const started = await musicManager.playMusic('pokemon_theme');
                musicBtn.textContent = started ? ' Pause Music' : ' Music blocked - click again';
            }
        });
    }
});

// in app.js, on the Exit form
document.querySelector('form[action$="/pokemon/exit"]')?.addEventListener('submit', () => {
    BackgroundMusicManager.getInstance().stopMusic();
});