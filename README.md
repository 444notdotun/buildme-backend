# BuildMe Backend — Deployment Guide

## Quick Deploy to Railway (Free)

### Step 1 — Push to GitHub
```bash
cd buildme-backend
git init
git add .
git commit -m "Initial BuildMe backend"
git remote add origin https://github.com/444notdotun/buildme-backend.git
git push -u origin main
```

### Step 2 — Deploy on Railway
1. Go to railway.app
2. Click "New Project" → "Deploy from GitHub repo"
3. Select buildme-backend repo
4. Railway auto-detects Spring Boot

### Step 3 — Add PostgreSQL
1. In Railway project → "New" → "PostgreSQL"
2. Copy the `DATABASE_URL` from PostgreSQL service

### Step 4 — Set Environment Variables
In Railway → your service → Variables:
```
DATABASE_URL=postgresql://...  (from Railway PostgreSQL)
CLAUDE_API_KEY=your_claude_api_key
PORT=8080
SCHEDULING_ENABLED=true
FRONTEND_URL=*
```

### Step 5 — Run Schema
Connect to your Railway PostgreSQL and run:
```
src/main/resources/schema.sql
```

### Step 6 — Get your backend URL
Railway gives you: `https://buildme-backend-production.up.railway.app`

Update your React frontend:
```javascript
const BACKEND_URL = "https://buildme-backend-production.up.railway.app";
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/health | Health check + current date/time |
| GET | /api/schedule/today | Real-time date awareness |
| GET | /api/profile | Get user profile |
| PUT | /api/profile | Update profile |
| GET | /api/plan?energy=HIGH&minutes=120 | Generate daily plan |
| POST | /api/progress | Log daily progress |
| GET | /api/progress | Get recent progress |
| POST | /api/dsa | Log DSA solution |
| GET | /api/dsa | Get DSA stats |
| GET | /api/challenges/weekly | Get weekly challenge |
| POST | /api/challenges/evaluate | Evaluate answer |
| GET | /api/challenges/interest | Get interest of week |
| POST | /api/mentor/chat | Send mentor message |
| GET | /api/mentor/history | Get chat history |
| GET | /api/opportunities | Get opportunities |
| PUT | /api/opportunities/{id}/seen | Mark seen |
| GET | /api/open-source | Get open source opps |
| POST | /api/search/run | Trigger manual search |
| POST | /api/commit/generate | Generate session commit |
| GET | /api/commit/latest | Get latest commit |
| GET | /api/notifications | Get notifications |
| POST | /api/auth/verify | Verify PIN |
| POST | /api/auth/pin/set | Set new PIN |

## Scheduled Tasks

| Task | Schedule | What it does |
|------|----------|--------------|
| Opportunity Search | Every 5 hours | Search jobs, scholarships, open source |
| Weekly Challenge | Monday 8am Lagos | Auto-generate weekly challenge |
| Interest of Week | Sunday 10am Lagos | Auto-generate interesting topic |
| Monthly Reflection | 1st of month 9am | Flag monthly reflection due |
| Era Review | Jan/Apr/Jul/Oct 1st | Flag 3-month era review |
| EOD Commit | 8pm daily Lagos | Auto-generate end of day commit |
