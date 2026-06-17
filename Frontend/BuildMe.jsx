import { useState, useEffect } from "react";

const API = "https://buildme-backend-183135031185.us-central1.run.app";

async function api(path, options = {}) {
  const res = await fetch(`${API}${path}`, {
    headers: { "Content-Type": "application/json", ...options.headers },
    ...options,
  });
  if (!res.ok) throw new Error(`${res.status}`);
  if (res.status === 204) return null;
  return res.json();
}

const styles = `
  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Barlow+Condensed:wght@700;800;900&display=swap');
  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
  :root {
    --black: #0A0A0A; --surface: #141414; --surface2: #1E1E1E;
    --border: #2A2A2A; --lime: #C8F135; --lime-dim: #9BBF20;
    --white: #F0EDE6; --grey: #888; --red: #FF4444; --blue: #4DA6FF;
    --font-display: 'Barlow Condensed', Impact, sans-serif;
    --font-body: 'Inter', system-ui, sans-serif;
  }
  body { background: var(--black); color: var(--white); font-family: var(--font-body); min-height: 100vh; }
  .app { display: flex; min-height: 100vh; }

  /* SIDEBAR */
  .sidebar {
    width: 220px; flex-shrink: 0; background: var(--surface);
    border-right: 1px solid var(--border); display: flex;
    flex-direction: column; position: sticky; top: 0; height: 100vh; overflow-y: auto;
  }
  .sidebar-logo { padding: 24px 20px 20px; font-family: var(--font-display); font-size: 22px; font-weight: 900; letter-spacing: 2px; text-transform: uppercase; border-bottom: 1px solid var(--border); }
  .sidebar-logo span { color: var(--lime); }
  .sidebar-nav { padding: 12px 0; flex: 1; }
  .nav-item { display: flex; align-items: center; gap: 10px; padding: 10px 20px; font-size: 14px; cursor: pointer; transition: all 0.15s; border-left: 3px solid transparent; color: var(--grey); }
  .nav-item:hover { color: var(--white); background: var(--surface2); }
  .nav-item.active { color: var(--lime); border-left-color: var(--lime); background: rgba(200,241,53,0.05); }
  .nav-icon { font-size: 18px; width: 20px; text-align: center; }
  .nav-badge { margin-left: auto; background: var(--red); color: #fff; font-size: 10px; font-weight: 700; padding: 2px 6px; border-radius: 10px; }
  .sidebar-footer { padding: 16px 20px; border-top: 1px solid var(--border); }
  .sidebar-date { font-size: 12px; color: var(--grey); margin-bottom: 4px; }
  .sidebar-day { font-family: var(--font-display); font-size: 18px; font-weight: 700; color: var(--lime); }

  /* MAIN */
  .main { flex: 1; overflow-y: auto; min-width: 0; }
  .page { padding: 32px 36px; max-width: 1100px; }
  .page-header { margin-bottom: 28px; }
  .page-title { font-family: var(--font-display); font-size: 40px; font-weight: 900; text-transform: uppercase; line-height: 1; margin-bottom: 6px; }
  .page-title span { color: var(--lime); }
  .page-sub { font-size: 14px; color: var(--grey); }

  /* CARDS */
  .card { background: var(--surface); border: 1px solid var(--border); border-radius: 12px; padding: 20px; }
  .card-label { font-size: 11px; font-weight: 600; letter-spacing: 2px; text-transform: uppercase; color: var(--lime); margin-bottom: 10px; }
  .card-title { font-size: 18px; font-weight: 600; margin-bottom: 6px; }
  .card-sub { font-size: 13px; color: var(--grey); line-height: 1.5; }

  /* GRID */
  .grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
  .grid-3 { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
  .grid-4 { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }

  /* STAT */
  .stat-card { background: var(--surface); border: 1px solid var(--border); border-radius: 12px; padding: 18px; }
  .stat-label { font-size: 12px; color: var(--grey); margin-bottom: 6px; }
  .stat-value { font-family: var(--font-display); font-size: 36px; font-weight: 900; color: var(--lime); line-height: 1; }
  .stat-unit { font-size: 13px; color: var(--grey); margin-top: 4px; }

  /* BUTTONS */
  .btn { display: inline-flex; align-items: center; gap: 6px; padding: 10px 18px; border-radius: 8px; font-family: var(--font-body); font-size: 14px; font-weight: 600; cursor: pointer; border: none; transition: all 0.2s; }
  .btn-lime { background: var(--lime); color: var(--black); }
  .btn-lime:hover { background: var(--lime-dim); }
  .btn-ghost { background: transparent; color: var(--white); border: 1px solid var(--border); }
  .btn-ghost:hover { border-color: var(--lime); color: var(--lime); }
  .btn-sm { padding: 6px 12px; font-size: 12px; }
  .btn:disabled { opacity: 0.4; cursor: not-allowed; }

  /* FORM */
  .form-group { margin-bottom: 16px; }
  .form-label { display: block; font-size: 12px; font-weight: 600; letter-spacing: 1px; text-transform: uppercase; color: var(--grey); margin-bottom: 6px; }
  .form-input, .form-select, .form-textarea { width: 100%; padding: 10px 14px; background: var(--surface2); border: 1px solid var(--border); border-radius: 8px; color: var(--white); font-family: var(--font-body); font-size: 14px; outline: none; transition: border-color 0.2s; }
  .form-input:focus, .form-select:focus, .form-textarea:focus { border-color: var(--lime); }
  .form-textarea { resize: vertical; min-height: 80px; }
  .form-select option { background: var(--surface); }

  /* TAGS */
  .tag { display: inline-flex; align-items: center; gap: 4px; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; }
  .tag-lime { background: rgba(200,241,53,0.12); color: var(--lime); }
  .tag-blue { background: rgba(77,166,255,0.12); color: var(--blue); }
  .tag-red { background: rgba(255,68,68,0.12); color: var(--red); }
  .tag-grey { background: var(--surface2); color: var(--grey); }

  /* LIST ITEMS */
  .list-item { display: flex; align-items: flex-start; gap: 14px; padding: 14px 0; border-bottom: 1px solid var(--border); }
  .list-item:last-child { border-bottom: none; }
  .list-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0; background: rgba(200,241,53,0.08); }
  .list-content { flex: 1; min-width: 0; }
  .list-title { font-size: 14px; font-weight: 600; margin-bottom: 3px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
  .list-sub { font-size: 12px; color: var(--grey); }
  .list-actions { display: flex; gap: 6px; align-items: center; flex-shrink: 0; }

  /* NOTIFICATION DOT */
  .notif-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--lime); flex-shrink: 0; margin-top: 5px; }

  /* PROGRESS BAR */
  .progress-bar { height: 6px; background: var(--surface2); border-radius: 3px; overflow: hidden; }
  .progress-fill { height: 100%; background: var(--lime); border-radius: 3px; transition: width 0.5s; }

  /* COMMIT BOX */
  .commit-box { background: var(--surface2); border: 1px solid var(--border); border-radius: 8px; padding: 14px; font-family: monospace; font-size: 12px; color: var(--grey); line-height: 1.6; white-space: pre-wrap; word-break: break-word; max-height: 300px; overflow-y: auto; }

  /* SPINNER */
  .spinner { display: inline-block; width: 14px; height: 14px; border: 2px solid transparent; border-top-color: currentColor; border-radius: 50%; animation: spin 0.7s linear infinite; }
  @keyframes spin { to { transform: rotate(360deg); } }

  /* EMPTY */
  .empty { text-align: center; padding: 48px 20px; color: var(--grey); }
  .empty-icon { font-size: 36px; margin-bottom: 12px; }
  .empty-text { font-size: 15px; color: var(--white); margin-bottom: 6px; }

  /* SUCCESS */
  .success-toast { position: fixed; bottom: 24px; right: 24px; background: var(--lime); color: var(--black); padding: 12px 20px; border-radius: 8px; font-size: 14px; font-weight: 600; z-index: 999; animation: slideUp 0.3s ease; }
  @keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }

  .divider { height: 1px; background: var(--border); margin: 20px 0; }
  .flex { display: flex; } .gap-8 { gap: 8px; } .gap-12 { gap: 12px; } .items-center { align-items: center; }
  .mt-16 { margin-top: 16px; } .mt-20 { margin-top: 20px; } .mb-16 { margin-bottom: 16px; }
  .text-lime { color: var(--lime); } .text-grey { color: var(--grey); } .text-sm { font-size: 13px; }
  .font-mono { font-family: monospace; }

  @media (max-width: 768px) {
    .sidebar { display: none; }
    .page { padding: 20px; }
    .grid-2, .grid-3, .grid-4 { grid-template-columns: 1fr; }
  }
`;

function Toast({ msg, onDone }) {
  useEffect(() => { const t = setTimeout(onDone, 2500); return () => clearTimeout(t); }, []);
  return <div className="success-toast">{msg}</div>;
}

function useApi(path, deps = []) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const refresh = () => { setLoading(true); api(path).then(setData).catch(() => {}).finally(() => setLoading(false)); };
  useEffect(refresh, deps);
  return { data, loading, refresh };
}

// ─── DASHBOARD ────────────────────────────────────────────────
function Dashboard({ onNav }) {
  const { data: schedule } = useApi("/api/schedule/today");
  const { data: profile } = useApi("/api/profile");
  const { data: notifs } = useApi("/api/notifications");
  const { data: progress } = useApi("/api/progress?limit=5");
  const [searching, setSearching] = useState(false);
  const [toast, setToast] = useState("");

  const runSearch = async () => {
    setSearching(true);
    try { await api("/api/search/run", { method: "POST" }); setToast("Search started in background!"); }
    catch (e) { setToast("Search triggered."); }
    finally { setSearching(false); }
  };

  const chevTotal = profile?.data?.cheveningHours || 0;
  const chevPct = Math.min(100, Math.round((chevTotal / 2800) * 100));

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title">Build<span>Me</span></div>
        <div className="page-sub">{schedule?.data?.dayOfWeek}, {schedule?.data?.today} · {schedule?.data?.timeZone}</div>
      </div>

      <div className="grid-4 mb-16">
        <div className="stat-card">
          <div className="stat-label">Chevening Hours</div>
          <div className="stat-value">{chevTotal}</div>
          <div className="stat-unit">/ 2800 target</div>
          <div className="progress-bar mt-16" style={{ marginTop: 10 }}>
            <div className="progress-fill" style={{ width: `${chevPct}%` }} />
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Unread Notifications</div>
          <div className="stat-value">{schedule?.data?.unreadNotifications ?? "—"}</div>
          <div className="stat-unit">alerts waiting</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Level</div>
          <div className="stat-value" style={{ fontSize: 22, paddingTop: 6 }}>{profile?.data?.level || "Junior"}</div>
          <div className="stat-unit">{profile?.data?.currentRole || "Backend Engineer"}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Weekly Challenge</div>
          <div className="stat-value" style={{ fontSize: 22, paddingTop: 6 }}>{schedule?.data?.weeklyChallengeReady ? "Ready" : "Pending"}</div>
          <div className="stat-unit" style={{ color: schedule?.data?.weeklyChallengeReady ? "var(--lime)" : "var(--grey)" }}>
            {schedule?.data?.weekKey}
          </div>
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-label">Recent Progress</div>
          {progress?.data?.length ? progress.data.slice(0, 4).map((p, i) => (
            <div key={i} className="list-item">
              <div className="list-icon">📦</div>
              <div className="list-content">
                <div className="list-title">{p.shipped || "Session logged"}</div>
                <div className="list-sub">{p.energyLevel} energy · {p.availableMinutes} min · {p.date}</div>
              </div>
            </div>
          )) : <div className="empty" style={{ padding: 24 }}><div className="text-grey text-sm">No progress logged yet</div></div>}
          <button className="btn btn-ghost btn-sm mt-16" onClick={() => onNav("progress")}>View all →</button>
        </div>

        <div className="card">
          <div className="card-label">Quick Actions</div>
          <div style={{ display: "flex", flexDirection: "column", gap: 10, marginTop: 8 }}>
            <button className="btn btn-lime" onClick={runSearch} disabled={searching} style={{ justifyContent: "flex-start" }}>
              {searching ? <span className="spinner" /> : "🔍"} Run opportunity search
            </button>
            <button className="btn btn-ghost" onClick={() => onNav("commit")} style={{ justifyContent: "flex-start" }}>
              📋 Generate session commit
            </button>
            <button className="btn btn-ghost" onClick={() => onNav("progress")} style={{ justifyContent: "flex-start" }}>
              📈 Log today's progress
            </button>
            <button className="btn btn-ghost" onClick={() => onNav("opportunities")} style={{ justifyContent: "flex-start" }}>
              💼 View opportunities
            </button>
          </div>
          <div className="divider" />
          <div className="card-label">Notifications</div>
          {notifs?.data?.slice(0, 3).map((n, i) => (
            <div key={i} className="list-item" style={{ paddingTop: 8, paddingBottom: 8 }}>
              <div className="notif-dot" />
              <div className="list-content">
                <div className="list-title" style={{ fontSize: 13 }}>{n.message || n.title}</div>
                <div className="list-sub">{n.type}</div>
              </div>
            </div>
          ))}
          <button className="btn btn-ghost btn-sm mt-16" onClick={() => onNav("notifications")}>All notifications →</button>
        </div>
      </div>
    </div>
  );
}

// ─── PROGRESS ─────────────────────────────────────────────────
function Progress() {
  const { data, loading, refresh } = useApi("/api/progress?limit=20");
  const [form, setForm] = useState({ shipped: "", energy: "HIGH", minutes: 60, concept: "" });
  const [saving, setSaving] = useState(false);
  const [toast, setToast] = useState("");

  const submit = async () => {
    setSaving(true);
    try {
      await api("/api/progress", { method: "POST", body: JSON.stringify(form) });
      setToast("Progress logged!");
      setForm({ shipped: "", energy: "HIGH", minutes: 60, concept: "" });
      refresh();
    } catch (e) { setToast("Failed to log progress"); }
    finally { setSaving(false); }
  };

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title">Progress <span>Log</span></div>
        <div className="page-sub">Track what you shipped and learned today.</div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-label">Log Today</div>
          <div className="form-group">
            <label className="form-label">What did you ship?</label>
            <input className="form-input" placeholder="Ghost Coach, BuildMe frontend..." value={form.shipped} onChange={e => setForm(f => ({ ...f, shipped: e.target.value }))} />
          </div>
          <div className="form-group">
            <label className="form-label">Concept studied</label>
            <input className="form-input" placeholder="@Transactional, Redis SETNX..." value={form.concept} onChange={e => setForm(f => ({ ...f, concept: e.target.value }))} />
          </div>
          <div className="grid-2">
            <div className="form-group">
              <label className="form-label">Energy level</label>
              <select className="form-select" value={form.energy} onChange={e => setForm(f => ({ ...f, energy: e.target.value }))}>
                <option value="HIGH">High</option>
                <option value="MEDIUM">Medium</option>
                <option value="LOW">Low</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Minutes available</label>
              <input className="form-input" type="number" value={form.minutes} onChange={e => setForm(f => ({ ...f, minutes: +e.target.value }))} />
            </div>
          </div>
          <button className="btn btn-lime" onClick={submit} disabled={saving || !form.shipped}>
            {saving ? <span className="spinner" /> : "Log Progress"}
          </button>
        </div>

        <div className="card">
          <div className="card-label">Recent Sessions</div>
          {loading ? <div className="text-grey text-sm">Loading...</div> : data?.data?.length ? data.data.map((p, i) => (
            <div key={i} className="list-item">
              <div className="list-icon">📦</div>
              <div className="list-content">
                <div className="list-title">{p.shipped || "—"}</div>
                <div className="list-sub">{p.date} · {p.energyLevel} energy · {p.availableMinutes} min</div>
                {p.shipped && <div className="tag tag-lime" style={{ marginTop: 4, fontSize: 10 }}>{p.shipped}</div>}
              </div>
            </div>
          )) : <div className="empty" style={{ padding: 20 }}><div className="text-grey text-sm">Nothing logged yet</div></div>}
        </div>
      </div>
    </div>
  );
}

// ─── OPPORTUNITIES ────────────────────────────────────────────
function Opportunities() {
  const [seen, setSeen] = useState(false);
  const { data, loading, refresh } = useApi(`/api/opportunities?seen=${seen}`, [seen]);
  const [toast, setToast] = useState("");

  const markSeen = async (id) => {
    try { await api(`/api/opportunities/${id}/seen`, { method: "PUT" }); setToast("Marked as seen"); refresh(); }
    catch (e) {}
  };

  const typeColor = (t) => {
    if (!t) return "tag-grey";
    if (t.toLowerCase().includes("job")) return "tag-lime";
    if (t.toLowerCase().includes("scholar")) return "tag-blue";
    return "tag-grey";
  };

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title"><span>Opportunities</span></div>
        <div className="page-sub">Jobs, scholarships and fellowships curated for your trajectory.</div>
      </div>

      <div className="flex gap-8 mb-16">
        <button className={`btn ${!seen ? "btn-lime" : "btn-ghost"}`} onClick={() => setSeen(false)}>Unseen</button>
        <button className={`btn ${seen ? "btn-lime" : "btn-ghost"}`} onClick={() => setSeen(true)}>Seen</button>
      </div>

      <div className="card">
        {loading ? <div className="text-grey text-sm">Loading...</div>
          : data?.data?.length ? data.data.map((o, i) => (
            <div key={i} className="list-item">
              <div className="list-icon">💼</div>
              <div className="list-content">
                <div className="list-title">{o.title || o.name || "Opportunity"}</div>
                <div className="list-sub">{o.company || o.source} · {o.location || ""}</div>
                <div className="flex gap-8" style={{ marginTop: 6 }}>
                  <span className={`tag ${typeColor(o.type)}`}>{o.type || "OPPORTUNITY"}</span>
                  {o.deadline && <span className="tag tag-grey">{o.deadline}</span>}
                </div>
              </div>
              <div className="list-actions">
                {o.url && <a href={o.url} target="_blank" rel="noreferrer" className="btn btn-ghost btn-sm">View</a>}
                {!seen && <button className="btn btn-ghost btn-sm" onClick={() => markSeen(o.id)}>✓</button>}
              </div>
            </div>
          )) : (
            <div className="empty">
              <div className="empty-icon">🎯</div>
              <div className="empty-text">No {seen ? "seen" : "new"} opportunities</div>
              <div className="text-grey text-sm">Run a search from the dashboard to populate this list.</div>
            </div>
          )}
      </div>
    </div>
  );
}

// ─── OPEN SOURCE ──────────────────────────────────────────────
function OpenSource() {
  const [seen, setSeen] = useState(false);
  const { data, loading, refresh } = useApi(`/api/open-source?seen=${seen}`, [seen]);
  const [toast, setToast] = useState("");

  const markSeen = async (id) => {
    try { await api(`/api/open-source/${id}/seen`, { method: "PUT" }); setToast("Marked as seen"); refresh(); }
    catch (e) {}
  };

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title">Open <span>Source</span></div>
        <div className="page-sub">Issues and PRs to build your public engineering profile.</div>
      </div>

      <div className="flex gap-8 mb-16">
        <button className={`btn ${!seen ? "btn-lime" : "btn-ghost"}`} onClick={() => setSeen(false)}>Unseen</button>
        <button className={`btn ${seen ? "btn-lime" : "btn-ghost"}`} onClick={() => setSeen(true)}>Seen</button>
      </div>

      <div className="card">
        {loading ? <div className="text-grey text-sm">Loading...</div>
          : data?.data?.length ? data.data.map((o, i) => (
            <div key={i} className="list-item">
              <div className="list-icon">⭐</div>
              <div className="list-content">
                <div className="list-title">{o.title || o.repoName || "Issue"}</div>
                <div className="list-sub">{o.repoUrl || o.language} · {o.stars ? `★ ${o.stars}` : ""}</div>
                <div className="flex gap-8" style={{ marginTop: 6 }}>
                  {o.language && <span className="tag tag-blue">{o.language}</span>}
                  {o.difficulty && <span className="tag tag-grey">{o.difficulty}</span>}
                </div>
              </div>
              <div className="list-actions">
                {o.url && <a href={o.url} target="_blank" rel="noreferrer" className="btn btn-ghost btn-sm">View</a>}
                {!seen && <button className="btn btn-ghost btn-sm" onClick={() => markSeen(o.id)}>✓</button>}
              </div>
            </div>
          )) : (
            <div className="empty">
              <div className="empty-icon">💻</div>
              <div className="empty-text">No open source items</div>
              <div className="text-grey text-sm">Run a search to find issues matching your stack.</div>
            </div>
          )}
      </div>
    </div>
  );
}

// ─── COMMIT ───────────────────────────────────────────────────
function Commit() {
  const { data: latest, loading, refresh } = useApi("/api/commit/latest");
  const [generating, setGenerating] = useState(false);
  const [toast, setToast] = useState("");
  const [copied, setCopied] = useState(false);

  const generate = async () => {
    setGenerating(true);
    try {
      await api("/api/commit/generate", { method: "POST" });
      setToast("Session commit generated!");
      refresh();
    } catch (e) { setToast("Failed to generate commit"); }
    finally { setGenerating(false); }
  };

  const copy = () => {
    const text = latest?.data?.copyablePrompt;
    if (text) { navigator.clipboard.writeText(text); setCopied(true); setTimeout(() => setCopied(false), 2000); }
  };

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title">Session <span>Commit</span></div>
        <div className="page-sub">Generate your master context prompt to carry into the next session.</div>
      </div>

      <div className="card mb-16" style={{ marginBottom: 16 }}>
        <div className="flex gap-8 items-center mb-16">
          <button className="btn btn-lime" onClick={generate} disabled={generating}>
            {generating ? <><span className="spinner" /> Generating...</> : "Generate Commit"}
          </button>
          {latest?.data && (
            <button className="btn btn-ghost" onClick={copy}>
              {copied ? "✓ Copied!" : "Copy to clipboard"}
            </button>
          )}
        </div>
        {latest?.data?.generatedAt && (
          <div className="text-grey text-sm" style={{ marginBottom: 12 }}>
            Last generated: {new Date(latest.data.generatedAt).toLocaleString("en-GB", { day: "numeric", month: "short", hour: "2-digit", minute: "2-digit" })}
          </div>
        )}
        {loading ? <div className="text-grey text-sm">Loading...</div>
          : latest?.data?.copyablePrompt ? (
            <div className="commit-box">{latest.data.copyablePrompt}</div>
          ) : (
            <div className="empty" style={{ padding: 32 }}>
              <div className="empty-icon">📋</div>
              <div className="empty-text">No commit generated yet</div>
              <div className="text-grey text-sm">Click Generate to create your session context.</div>
            </div>
          )}
      </div>
    </div>
  );
}

// ─── NOTIFICATIONS ────────────────────────────────────────────
function Notifications() {
  const [read, setRead] = useState(false);
  const { data, loading, refresh } = useApi(`/api/notifications?read=${read}`, [read]);
  const [toast, setToast] = useState("");

  const markRead = async (id) => {
    try { await api(`/api/notifications/${id}/read`, { method: "PUT" }); setToast("Marked as read"); refresh(); }
    catch (e) {}
  };

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title"><span>Notifications</span></div>
        <div className="page-sub">Alerts, reminders and system updates.</div>
      </div>

      <div className="flex gap-8 mb-16">
        <button className={`btn ${!read ? "btn-lime" : "btn-ghost"}`} onClick={() => setRead(false)}>Unread</button>
        <button className={`btn ${read ? "btn-lime" : "btn-ghost"}`} onClick={() => setRead(true)}>Read</button>
      </div>

      <div className="card">
        {loading ? <div className="text-grey text-sm">Loading...</div>
          : data?.data?.length ? data.data.map((n, i) => (
            <div key={i} className="list-item">
              {!read && <div className="notif-dot" />}
              <div className="list-content">
                <div className="list-title">{n.message || n.title}</div>
                <div className="list-sub">{n.type} · {n.createdAt ? new Date(n.createdAt).toLocaleDateString("en-GB") : ""}</div>
              </div>
              {!read && (
                <button className="btn btn-ghost btn-sm" onClick={() => markRead(n.id)}>Mark read</button>
              )}
            </div>
          )) : (
            <div className="empty">
              <div className="empty-icon">🔔</div>
              <div className="empty-text">No {read ? "read" : "unread"} notifications</div>
            </div>
          )}
      </div>
    </div>
  );
}

// ─── PROFILE ──────────────────────────────────────────────────
function Profile() {
  const { data, loading, refresh } = useApi("/api/profile");
  const [form, setForm] = useState(null);
  const [saving, setSaving] = useState(false);
  const [toast, setToast] = useState("");

  useEffect(() => { if (data?.data) setForm({ ...data.data }); }, [data]);

  const save = async () => {
    setSaving(true);
    try { await api("/api/profile", { method: "PUT", body: JSON.stringify(form) }); setToast("Profile updated!"); refresh(); }
    catch (e) { setToast("Failed to update profile"); }
    finally { setSaving(false); }
  };

  if (loading || !form) return <div className="page"><div className="text-grey">Loading...</div></div>;

  const p = form;
  const chevPct = Math.min(100, Math.round(((p.cheveningHours || 0) / 2800) * 100));

  return (
    <div className="page">
      {toast && <Toast msg={toast} onDone={() => setToast("")} />}
      <div className="page-header">
        <div className="page-title">My <span>Profile</span></div>
        <div className="page-sub">Your career intelligence profile — the single source of truth.</div>
      </div>

      <div className="grid-2">
        <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
          <div className="card">
            <div className="card-label">Identity</div>
            <div className="form-group">
              <label className="form-label">Full name</label>
              <input className="form-input" value={p.name || ""} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Current role</label>
              <input className="form-input" value={p.currentRole || ""} onChange={e => setForm(f => ({ ...f, currentRole: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Level</label>
              <select className="form-select" value={p.level || "JUNIOR"} onChange={e => setForm(f => ({ ...f, level: e.target.value }))}>
                <option value="JUNIOR">Junior</option>
                <option value="MID">Mid-level</option>
                <option value="SENIOR">Senior</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" value={p.email || ""} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
            </div>
          </div>

          <div className="card">
            <div className="card-label">Chevening Progress</div>
            <div className="flex items-center gap-8" style={{ marginBottom: 10 }}>
              <span className="text-lime" style={{ fontFamily: "var(--font-display)", fontSize: 36, fontWeight: 900 }}>{p.cheveningHours || 0}</span>
              <span className="text-grey text-sm">/ 2800 hours</span>
            </div>
            <div className="progress-bar">
              <div className="progress-fill" style={{ width: `${chevPct}%` }} />
            </div>
            <div className="text-grey text-sm" style={{ marginTop: 8 }}>{chevPct}% complete · {2800 - (p.cheveningHours || 0)} hours remaining</div>
            <div className="form-group" style={{ marginTop: 16 }}>
              <label className="form-label">Has active job?</label>
              <select className="form-select" value={p.hasJob ? "true" : "false"} onChange={e => setForm(f => ({ ...f, hasJob: e.target.value === "true" }))}>
                <option value="true">Yes — auto-log 8hrs/day</option>
                <option value="false">No</option>
              </select>
            </div>
          </div>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
          <div className="card">
            <div className="card-label">Career Goals</div>
            <div className="form-group">
              <label className="form-label">Target role</label>
              <input className="form-input" value={p.targetRole || ""} onChange={e => setForm(f => ({ ...f, targetRole: e.target.value }))} placeholder="e.g. Senior Backend Engineer" />
            </div>
            <div className="form-group">
              <label className="form-label">GitHub</label>
              <input className="form-input" value={p.githubUrl || ""} onChange={e => setForm(f => ({ ...f, githubUrl: e.target.value }))} placeholder="github.com/username" />
            </div>
            <div className="form-group">
              <label className="form-label">LinkedIn</label>
              <input className="form-input" value={p.linkedinUrl || ""} onChange={e => setForm(f => ({ ...f, linkedinUrl: e.target.value }))} placeholder="linkedin.com/in/username" />
            </div>
          </div>

          <div className="card">
            <div className="card-label">Open Loops</div>
            <textarea className="form-textarea" style={{ minHeight: 120 }} value={p.openLoops || ""} onChange={e => setForm(f => ({ ...f, openLoops: e.target.value }))} placeholder="Ghost Coach deployment, Wallet system design, Sunday quiz..." />
          </div>

          <button className="btn btn-lime" onClick={save} disabled={saving}>
            {saving ? <><span className="spinner" /> Saving...</> : "Save Profile"}
          </button>
        </div>
      </div>
    </div>
  );
}

// ─── APP SHELL ────────────────────────────────────────────────
const NAV = [
  { id: "dashboard", label: "Dashboard", icon: "🏠" },
  { id: "progress", label: "Progress", icon: "📈" },
  { id: "opportunities", label: "Opportunities", icon: "💼" },
  { id: "opensource", label: "Open Source", icon: "⭐" },
  { id: "commit", label: "Session Commit", icon: "📋" },
  { id: "notifications", label: "Notifications", icon: "🔔" },
  { id: "profile", label: "Profile", icon: "👤" },
];

export default function App() {
  const [page, setPage] = useState("dashboard");
  const [unread, setUnread] = useState(0);

  useEffect(() => {
    api("/api/schedule/today").then(r => setUnread(r?.data?.unreadNotifications || 0)).catch(() => {});
  }, [page]);

  const pages = {
    dashboard: <Dashboard onNav={setPage} />,
    progress: <Progress />,
    opportunities: <Opportunities />,
    opensource: <OpenSource />,
    commit: <Commit />,
    notifications: <Notifications />,
    profile: <Profile />,
  };

  return (
    <>
      <style>{styles}</style>
      <div className="app">
        <aside className="sidebar">
          <div className="sidebar-logo">Build<span>Me</span></div>
          <nav className="sidebar-nav">
            {NAV.map(n => (
              <div key={n.id} className={`nav-item${page === n.id ? " active" : ""}`} onClick={() => setPage(n.id)}>
                <span className="nav-icon">{n.icon}</span>
                {n.label}
                {n.id === "notifications" && unread > 0 && <span className="nav-badge">{unread}</span>}
              </div>
            ))}
          </nav>
          <div className="sidebar-footer">
            <div className="sidebar-date">{new Date().toLocaleDateString("en-GB", { day: "numeric", month: "short" })}</div>
            <div className="sidebar-day">{new Date().toLocaleDateString("en-GB", { weekday: "long" })}</div>
          </div>
        </aside>
        <main className="main">
          {pages[page]}
        </main>
      </div>
    </>
  );
}
