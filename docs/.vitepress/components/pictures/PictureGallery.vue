<script setup>
import { computed, ref } from 'vue'
import { useData, withBase } from 'vitepress'

const { lang } = useData()
const query = ref('')
const activeCategory = ref('all')

const images = [
  { src: '/gui-showcase.gif', category: 'interfaces', en: { title: 'Spawner control panel', description: 'Main interface for storage, upgrades, selling, and spawner controls.', tags: ['gui', 'menu', 'storage', 'sell'] }, vi: { title: 'Bảng điều khiển spawner', description: 'Giao diện chính cho kho, nâng cấp, bán vật phẩm và điều khiển spawner.', tags: ['giao diện', 'menu', 'kho', 'bán'] } },
  { src: '/spawner-stacking.gif', category: 'actions', en: { title: 'Stacking spawners', description: 'Adding multiple spawners to a single block without spawning extra mobs.', tags: ['stack', 'combine', 'farm', 'performance'] }, vi: { title: 'Xếp chồng spawner', description: 'Gộp nhiều spawner vào một block mà không tạo thêm mob.', tags: ['xếp chồng', 'gộp', 'farm', 'hiệu năng'] } },
  { src: '/spawner-list.gif', category: 'administration', en: { title: 'Server spawner browser', description: 'Browse, filter, and manage spawners across worlds from one place.', tags: ['list', 'world', 'admin', 'filter'] }, vi: { title: 'Danh sách spawner máy chủ', description: 'Duyệt, lọc và quản lý spawner ở nhiều world từ một nơi.', tags: ['danh sách', 'world', 'admin', 'lọc'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/4fbb3803988552890c1f8e1b687f16ad865992cd.webp', category: 'interfaces', en: { title: 'Paged item storage', description: 'A focused inventory view for stored drops and navigation between pages.', tags: ['inventory', 'items', 'pages', 'storage'] }, vi: { title: 'Kho vật phẩm nhiều trang', description: 'Kho tập trung cho vật phẩm đã lưu và điều hướng giữa các trang.', tags: ['túi đồ', 'vật phẩm', 'trang', 'kho'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/6606107b6566bffaead8c9f28c981cfd056fd79d.webp', category: 'administration', en: { title: 'Spawner management', description: 'Inspect and change stack size, state, and spawner-specific settings.', tags: ['settings', 'stack', 'manage', 'admin'] }, vi: { title: 'Quản lý spawner', description: 'Kiểm tra và thay đổi số lượng stack, trạng thái và thiết lập riêng.', tags: ['thiết lập', 'stack', 'quản lý', 'admin'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/8ca7ab0c2b17f5e7a9ce69bded34c0a3edde7078.webp', category: 'administration', en: { title: 'Cross-world overview', description: 'Find spawners by server, world, owner, and location.', tags: ['server', 'world', 'owner', 'location'] }, vi: { title: 'Tổng quan nhiều world', description: 'Tìm spawner theo máy chủ, world, chủ sở hữu và vị trí.', tags: ['máy chủ', 'world', 'chủ sở hữu', 'vị trí'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/df395dacedef2e793d131059b6d10ee8f9cd4d28_350.webp', category: 'actions', en: { title: 'Storage sorting', description: 'Sort stored drops so the item you need is easier to reach.', tags: ['sort', 'items', 'inventory', 'drops'] }, vi: { title: 'Sắp xếp kho', description: 'Sắp xếp vật phẩm để nhanh chóng tìm được món đồ cần dùng.', tags: ['sắp xếp', 'vật phẩm', 'túi đồ', 'drops'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/df8098897c88f1d02f8d26b70f4834c705cfe2fb.webp', category: 'bedrock', en: { title: 'Bedrock form UI', description: 'Native, touch-friendly controls for players joining through Geyser and Floodgate.', tags: ['bedrock', 'geyser', 'floodgate', 'mobile'] }, vi: { title: 'Form UI cho Bedrock', description: 'Điều khiển native, thân thiện với cảm ứng cho người chơi qua Geyser và Floodgate.', tags: ['bedrock', 'geyser', 'floodgate', 'di động'] } },
  { src: 'https://cdn.modrinth.com/data/9tQwxSFr/images/e5de039ba6be2cc100e1529d708a02e50b34a6f5.webp', category: 'actions', en: { title: 'Spawner visual effects', description: 'In-world feedback that makes spawner state and activity easy to recognize.', tags: ['effects', 'hologram', 'world', 'feedback'] }, vi: { title: 'Hiệu ứng spawner', description: 'Phản hồi trong world giúp nhận biết trạng thái và hoạt động của spawner.', tags: ['hiệu ứng', 'hologram', 'world', 'phản hồi'] } }
]

const isVi = computed(() => (lang.value || '').startsWith('vi'))
const localeKey = computed(() => isVi.value ? 'vi' : 'en')
const copy = computed(() => isVi.value
  ? { search: 'Tìm theo tên, tính năng hoặc giao diện...', label: 'Tìm ảnh SmartSpawner', all: 'Tất cả', interfaces: 'Giao diện', actions: 'Thao tác', administration: 'Quản trị', bedrock: 'Bedrock', count: (count) => `${count} ảnh`, open: 'Mở ảnh đầy đủ', emptyTitle: 'Không tìm thấy ảnh phù hợp', emptyText: 'Thử từ khóa ngắn hơn hoặc chọn tất cả danh mục.', clear: 'Xóa bộ lọc' }
  : { search: 'Search by name, feature, or interface...', label: 'Search SmartSpawner pictures', all: 'All', interfaces: 'Interfaces', actions: 'Actions', administration: 'Administration', bedrock: 'Bedrock', count: (count) => `${count} ${count === 1 ? 'picture' : 'pictures'}`, open: 'Open full picture', emptyTitle: 'No matching pictures', emptyText: 'Try a shorter keyword or show every category.', clear: 'Clear filters' })
const categories = ['all', 'interfaces', 'actions', 'administration', 'bedrock']

function normalize(value) {
  return value.toLocaleLowerCase(localeKey.value === 'vi' ? 'vi' : 'en').normalize('NFD').replace(/[\u0300-\u036f]/g, '')
}

const filteredImages = computed(() => {
  const term = normalize(query.value.trim())
  return images.filter((image) => {
    if (activeCategory.value !== 'all' && image.category !== activeCategory.value) return false
    if (!term) return true
    const content = image[localeKey.value]
    return normalize([content.title, content.description, ...content.tags].join(' ')).includes(term)
  })
})

const imageUrl = (src) => src.startsWith('/') ? withBase(src) : src
function clearFilters() { query.value = ''; activeCategory.value = 'all' }
</script>

<template>
  <section class="picture-gallery" aria-labelledby="picture-search-heading">
    <h2 id="picture-search-heading" class="sr-only">{{ copy.label }}</h2>
    <div class="picture-tools">
      <label class="picture-search">
        <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><circle cx="11" cy="11" r="7" /><path d="m20 20-3.7-3.7" /></svg>
        <span class="sr-only">{{ copy.label }}</span>
        <input v-model="query" type="search" :placeholder="copy.search" autocomplete="off">
      </label>
      <div class="picture-categories" role="group" :aria-label="isVi ? 'หมวดหมู่รูปภาพ' : 'Picture categories'">
        <button v-for="category in categories" :key="category" type="button" :class="{ active: activeCategory === category }" :aria-pressed="activeCategory === category" @click="activeCategory = category">
          {{ copy[category] }}
        </button>
      </div>
      <p class="picture-count" aria-live="polite">{{ copy.count(filteredImages.length) }}</p>
    </div>

    <div v-if="filteredImages.length" class="picture-grid">
      <figure v-for="image in filteredImages" :key="image.src" class="picture-item">
        <a class="picture-link" :href="imageUrl(image.src)" target="_blank" rel="noopener noreferrer" :aria-label="`${copy.open}: ${image[localeKey].title}`">
          <img :src="imageUrl(image.src)" :alt="image[localeKey].title" loading="lazy" decoding="async">
          <span class="picture-open" aria-hidden="true">{{ copy.open }} <svg viewBox="0 0 24 24" width="16" height="16"><path d="M7 17 17 7M8 7h9v9" /></svg></span>
        </a>
        <figcaption>
          <strong>{{ image[localeKey].title }}</strong>
          <span>{{ image[localeKey].description }}</span>
          <span class="picture-tags">{{ image[localeKey].tags.join(' · ') }}</span>
        </figcaption>
      </figure>
    </div>

    <div v-else class="picture-empty">
      <span class="picture-empty-icon" aria-hidden="true"><svg viewBox="0 0 24 24" width="26" height="26"><rect x="3" y="4" width="18" height="16" rx="2" /><circle cx="9" cy="10" r="2" /><path d="m21 15-4.5-4.5L6 20" /></svg></span>
      <h3>{{ copy.emptyTitle }}</h3>
      <p>{{ copy.emptyText }}</p>
      <button type="button" @click="clearFilters">{{ copy.clear }}</button>
    </div>
  </section>
</template>

<style scoped>
.picture-gallery { margin-top: 32px; }
.sr-only { position: absolute; width: 1px; height: 1px; padding: 0; overflow: hidden; clip: rect(0, 0, 0, 0); white-space: nowrap; border: 0; }
.picture-tools { position: sticky; top: calc(var(--vp-nav-height) + 12px); z-index: 5; display: grid; grid-template-areas: 'search count' 'categories categories'; grid-template-columns: minmax(0, 1fr) auto; gap: 10px 18px; align-items: center; margin-bottom: 30px; padding: 14px; border: 1px solid var(--vp-c-divider); border-radius: 16px; background: var(--vp-c-bg); box-shadow: 0 12px 36px color-mix(in oklch, var(--vp-c-text-1) 8%, transparent); }
.picture-search { grid-area: search; display: flex; align-items: center; gap: 10px; min-height: 46px; padding: 0 14px; border: 1px solid var(--vp-c-border); border-radius: 11px; background: var(--vp-c-bg-soft); color: var(--vp-c-text-3); transition: border-color 180ms ease, box-shadow 180ms ease, background-color 180ms ease; }
.picture-search:focus-within { border-color: var(--vp-c-brand-1); background: var(--vp-c-bg); box-shadow: 0 0 0 3px var(--vp-c-brand-soft); }
.picture-search svg, .picture-open svg, .picture-empty-icon svg { flex: 0 0 auto; fill: none; stroke: currentColor; stroke-width: 1.8; stroke-linecap: round; stroke-linejoin: round; }
.picture-search input { width: 100%; min-width: 0; padding: 0; border: 0; outline: 0; background: transparent; color: var(--vp-c-text-1); font: inherit; font-size: .94rem; }
.picture-search input::placeholder { color: var(--vp-c-text-3); }
.picture-categories { grid-area: categories; display: flex; flex-wrap: wrap; gap: 6px; }
.picture-categories button, .picture-empty button { min-height: 36px; padding: 0 12px; border: 1px solid transparent; border-radius: 9px; background: transparent; color: var(--vp-c-text-2); font: inherit; font-size: .82rem; font-weight: 650; cursor: pointer; transition: color 180ms ease, border-color 180ms ease, background-color 180ms ease; }
.picture-categories button:hover { background: var(--vp-c-bg-soft); color: var(--vp-c-text-1); }
.picture-categories button.active { border-color: color-mix(in oklch, var(--vp-c-brand-1) 30%, transparent); background: var(--vp-c-brand-soft); color: var(--vp-c-brand-1); }
.picture-categories button:focus-visible, .picture-empty button:focus-visible, .picture-link:focus-visible { outline: 2px solid var(--vp-c-brand-1); outline-offset: 3px; }
.picture-count { grid-area: count; margin: 0 2px; color: var(--vp-c-text-3); font-size: .78rem; font-weight: 600; white-space: nowrap; }
.picture-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 28px 18px; }
.picture-item { min-width: 0; margin: 0; }
.picture-link { position: relative; display: block; aspect-ratio: 16 / 10; overflow: hidden; border: 1px solid var(--vp-c-border); border-radius: 13px; background: oklch(21% .012 240); text-decoration: none; box-shadow: 0 5px 18px color-mix(in oklch, var(--vp-c-text-1) 7%, transparent); transition: border-color 200ms ease, box-shadow 200ms ease, transform 200ms cubic-bezier(.22, 1, .36, 1); }
.picture-link:hover { border-color: color-mix(in oklch, var(--vp-c-brand-1) 65%, var(--vp-c-border)); box-shadow: 0 12px 28px color-mix(in oklch, var(--vp-c-text-1) 13%, transparent); transform: translateY(-2px); }
.picture-link img { display: block; width: 100%; height: 100%; margin: 0; border-radius: 0; object-fit: contain; transition: transform 250ms cubic-bezier(.22, 1, .36, 1); }
.picture-link:hover img { transform: scale(1.015); }
.picture-open { position: absolute; right: 10px; bottom: 10px; display: inline-flex; align-items: center; gap: 6px; padding: 7px 9px; border: 1px solid oklch(100% 0 0 / 15%); border-radius: 8px; background: oklch(19% .01 240 / 88%); color: oklch(96% .01 240); font-size: .72rem; font-weight: 650; opacity: 0; transform: translateY(4px); transition: opacity 180ms ease, transform 180ms cubic-bezier(.22, 1, .36, 1); }
.picture-link:hover .picture-open, .picture-link:focus-visible .picture-open { opacity: 1; transform: translateY(0); }
.picture-item figcaption { display: grid; gap: 5px; padding: 13px 3px 0; }
.picture-item figcaption strong { color: var(--vp-c-text-1); font-size: .96rem; line-height: 1.4; }
.picture-item figcaption > span:not(.picture-tags) { color: var(--vp-c-text-2); font-size: .84rem; line-height: 1.55; }
.picture-tags { color: var(--vp-c-text-3); font-size: .72rem; line-height: 1.5; }
.picture-empty { display: grid; justify-items: center; min-height: 300px; padding: 56px 24px; border: 1px dashed var(--vp-c-divider); border-radius: 14px; text-align: center; }
.picture-empty-icon { display: grid; place-items: center; width: 52px; height: 52px; margin-bottom: 14px; border-radius: 12px; background: var(--vp-c-bg-soft); color: var(--vp-c-text-3); }
.picture-empty h3 { margin: 0; color: var(--vp-c-text-1); font-size: 1rem; }
.picture-empty p { max-width: 42ch; margin: 7px 0 18px; color: var(--vp-c-text-2); font-size: .88rem; }
.picture-empty button { border-color: var(--vp-c-border); background: var(--vp-c-bg-soft); color: var(--vp-c-text-1); }
@media (max-width: 760px) { .picture-tools { position: static; grid-template-areas: 'search' 'categories' 'count'; grid-template-columns: 1fr; } }
@media (max-width: 560px) { .picture-grid { grid-template-columns: 1fr; gap: 24px; } .picture-categories { flex-wrap: wrap; } .picture-categories button { flex: 0 0 auto; } .picture-open { opacity: 1; transform: none; } }
@media (prefers-reduced-motion: reduce) { .picture-link, .picture-link img, .picture-open { transition: none; } }
</style>
