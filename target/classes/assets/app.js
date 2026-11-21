/* Client-side navigation and local wiki search. No external dependencies. */
(() => {
  'use strict';
  const body = document.body;
  const dialog = document.getElementById('search-dialog');
  const searchInput = document.getElementById('global-search');
  const searchResults = document.getElementById('search-results');
  const nav = document.querySelector('.primary-nav');
  const navLinks = [...document.querySelectorAll('.primary-nav .nav-link')];
  const toggle = document.getElementById('toggle');
  const mobile = window.matchMedia('(max-width: 760px)');
  const storage = {
    get: (key) => {try{return localStorage.getItem(key);}catch{return null;}},
    set: (key,value) => {try{localStorage.setItem(key,value);}catch{}}
  };
  function collapsed() { return body.classList.contains('sidebar-collapsed'); }
  function setCollapsed(value) {
    body.classList.toggle('sidebar-collapsed',value);
    toggle?.setAttribute('aria-expanded',String(!value));
    if(!mobile.matches) storage.set('ps-sidebar-collapsed',String(value));
  }
  setCollapsed(mobile.matches || storage.get('ps-sidebar-collapsed') === 'true');
  // Sidebar is its own scroll container; preserve this scroll offset between page loads.
  // sessionStorage keeps the current browsing session separate from an older visit.
  const navScrollKey = 'ps-sidebar-scroll-position';
  const savedNavScroll = (() => { try { return sessionStorage.getItem(navScrollKey); } catch { return null; } })();
  function rememberSidebarScroll() {
    if (!nav) return;
    try { sessionStorage.setItem(navScrollKey, String(Math.round(nav.scrollTop))); } catch {}
  }
  nav?.addEventListener('scroll', rememberSidebarScroll, {passive: true});
  navLinks.forEach(link => link.addEventListener('click', rememberSidebarScroll));
  document.querySelectorAll('.brand, .nav-special a, .breadcrumb-home, .search-result')
    .forEach(link => link.addEventListener('click', rememberSidebarScroll));
  function restoreSidebarScroll() {
    if (!nav) return;
    if (savedNavScroll !== null && Number.isFinite(Number(savedNavScroll))) {
      nav.scrollTop = Math.max(0, Number(savedNavScroll));
    } else {
      // First visit: reveal the selected link *inside the nav only* without moving the page.
      const active = nav.querySelector('.nav-link.active');
      if (active) {
        const top = active.offsetTop - nav.offsetTop;
        if (top + active.offsetHeight > nav.clientHeight) {
          nav.scrollTop = top + active.offsetHeight - nav.clientHeight + 16;
        }
      }
    }
  }
  restoreSidebarScroll();
  requestAnimationFrame(restoreSidebarScroll);
  toggle?.addEventListener('click', () => setCollapsed(!collapsed()));
  document.getElementById('collapse')?.addEventListener('click', () => setCollapsed(true));
  document.getElementById('sidebar-shade')?.addEventListener('click', () => setCollapsed(true));
  mobile.addEventListener('change', e => setCollapsed(e.matches || storage.get('ps-sidebar-collapsed') === 'true'));


  const pages = navLinks.map((element) => ({
    element, title: element.dataset.title || element.textContent.trim(),
    category: element.dataset.category || 'Quick navigation', url: element.getAttribute('href')
  }));
  // The map appears in the quick navigation even though it has no numbered wiki section.
  let selection = 0;
  let matches = [];
  function renderResults(query) {
    const q = query.trim().toLowerCase();
    matches = pages.filter(p => !q || (p.title + ' ' + p.category).toLowerCase().includes(q)).slice(0,45);
    selection = 0;
    searchResults.replaceChildren();
    if(!matches.length){
      const no = document.createElement('div'); no.className='search-no-results';
      no.textContent='No matching pages. Try a broader search.';
      searchResults.append(no);return;
    }
    for(let i=0;i<matches.length;i++) {
      const page=matches[i], a=document.createElement('a');
      a.className='search-result'+(i===0?' highlighted':'');a.href=page.url;
      const pict=document.createElement('span');pict.className='search-result-icon';pict.innerHTML=page.element.querySelector('.nav-icon')?.innerHTML||'';
      const txt=document.createElement('span'); const name=document.createElement('strong');name.textContent=page.title;
      const cat=document.createElement('small');cat.textContent=page.category;txt.append(name,cat);
      const arrow=document.createElement('span');arrow.className='result-arrow';arrow.textContent='↗';
      a.append(pict,txt,arrow);a.addEventListener('pointerenter',()=>{selection=i;highlight()});searchResults.append(a);
    }
  }
  function highlight() {
    const elements = [...searchResults.querySelectorAll('.search-result')];
    elements.forEach((el,i) => el.classList.toggle('highlighted',i===selection));
    elements[selection]?.scrollIntoView({block:'nearest'});
  }
  function openSearch(){
    if(!dialog.open) dialog.showModal();
    searchInput.value='';renderResults('');searchInput.focus();
  }
  function closeSearch(){ if(dialog.open) dialog.close(); }
  document.querySelectorAll('[data-open-search]').forEach(btn => btn.addEventListener('click',openSearch));
  document.getElementById('search-close')?.addEventListener('click',closeSearch);
  searchInput?.addEventListener('input',() => renderResults(searchInput.value));
  searchInput?.addEventListener('keydown',event => {
    if(event.key==='ArrowDown'){event.preventDefault();selection=Math.min(selection+1,matches.length-1);highlight();}
    if(event.key==='ArrowUp'){event.preventDefault();selection=Math.max(selection-1,0);highlight();}
    if(event.key==='Enter'&&matches.length){event.preventDefault();window.location.assign(matches[selection].url);}
  });
  dialog?.addEventListener('click',e => {if(e.target===dialog) closeSearch();});
  document.addEventListener('keydown',event => {
    if((event.metaKey||event.ctrlKey)&&event.key.toLowerCase()==='k') {event.preventDefault();openSearch();}
    else if(event.key==='/'&&!dialog.open&&!/INPUT|TEXTAREA|SELECT/.test(document.activeElement?.tagName||'')) {
      event.preventDefault();openSearch();
    }
    else if(event.key==='Escape' && mobile.matches && !collapsed() && !dialog.open){setCollapsed(true);}
  });
  // Table of contents is generated automatically from actual page headings.
  const toc = document.getElementById('toc');
  const tocLinks = document.getElementById('toc-links');
  const headings = [...document.querySelectorAll('.page-content h2')].filter(h=>h.textContent.trim());
  if(!headings.length) toc?.remove();
  headings.forEach((h,i)=>{
    if(!h.id) h.id='section-'+(i+1);
    const link=document.createElement('a');link.href='#'+h.id;link.textContent=h.textContent.trim();
    tocLinks?.append(link);
  });
  // Deliberately do not call scrollIntoView: it can scroll the sidebar AND document.
})();
