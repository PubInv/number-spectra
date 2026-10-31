// Start with: node server.js

import express from "express";
import { fileURLToPath } from "node:url";

const app = express();

// --------------------------------------------------
// Welcome page and application
// --------------------------------------------------

const welcomeFile = fileURLToPath(new URL("./welcome.html", import.meta.url));

const applicationFile = fileURLToPath(new URL("./index.html", import.meta.url));

// Visitors arrive at the welcome page.
app.get(["/", "/index.html", "/welcome.html"], (req, res) => {
  res.sendFile(welcomeFile);
});

// The welcome page's Launch app button points here.
app.get(["/app", "/app/"], (req, res) => {
  res.sendFile(applicationFile);
});

// --------------------------------------------------
// OEIS helpers
// --------------------------------------------------

function toOeisId(raw) {
  // Accepts "40", "000040", "A40", or "A000040".
  const s = String(raw).trim().toUpperCase().replace(/^A/, "");

  if (!/^\d+$/.test(s)) return null;

  const padded = s.padStart(6, "0");
  return `A${padded}`;
}

function parseDataFieldToNumbers(dataStr) {
  return String(dataStr)
    .split(",")
    .map((x) => x.trim())
    .filter(Boolean)
    .map((x) => Number(x))
    .filter((n) => Number.isFinite(n));
}

// --------------------------------------------------
// Load one OEIS sequence
// Example: /oeis/40
// --------------------------------------------------

app.get("/oeis/:anum", async (req, res) => {
  const id = toOeisId(req.params.anum);

  if (!id) {
    return res.status(400).json({
      error: "Bad input. Use digits like 40, 000040, or A000040.",
    });
  }

  const url = `https://oeis.org/${id}?fmt=json`;

  try {
    const r = await fetch(url, {
      headers: {
        "User-Agent": "oeis-proxy/1.0",
      },
    });

    if (!r.ok) {
      return res.status(r.status).send(await r.text());
    }

    const json = await r.json();

    const dataStr = json?.data ?? json?.results?.[0]?.data;
    const name = json?.name ?? json?.results?.[0]?.name ?? "";

    if (!dataStr) {
      return res.status(502).json({
        error: "Could not find a data field in OEIS response",
        id,
        gotKeys: Object.keys(json || {}),
      });
    }

    const numbers = parseDataFieldToNumbers(dataStr);

    res.set("Access-Control-Allow-Origin", "*");

    return res.json({
      id,
      name,
      numbers,
      count: numbers.length,
    });
  } catch (e) {
    return res.status(500).json({
      error: String(e),
    });
  }
});

// --------------------------------------------------
// Load multiple OEIS sequences
// Example: /oeis?ids=40,52,A000045
// --------------------------------------------------

app.get("/oeis", async (req, res) => {
  const idsRaw = String(req.query.ids || "");

  const parts = idsRaw
    .split(/[\s,]+/)
    .map((s) => s.trim())
    .filter(Boolean);

  const oeisIds = parts.map(toOeisId).filter(Boolean);

  if (oeisIds.length === 0) {
    return res.status(400).json({
      error: "Provide ids query param like /oeis?ids=40,52,A000045",
    });
  }

  const out = [];

  for (const id of oeisIds) {
    const url = `https://oeis.org/${id}?fmt=json`;

    try {
      const r = await fetch(url, {
        headers: {
          "User-Agent": "oeis-proxy/1.0",
        },
      });

      if (!r.ok) {
        out.push({
          id,
          error: `HTTP ${r.status}`,
        });
        continue;
      }

      const json = await r.json();

      const dataStr = json?.data ?? json?.results?.[0]?.data;
      const name = json?.name ?? json?.results?.[0]?.name ?? "";

      if (!dataStr) {
        out.push({
          id,
          error: "Missing data field",
        });
        continue;
      }

      const numbers = parseDataFieldToNumbers(dataStr);

      out.push({
        id,
        name,
        numbers,
        count: numbers.length,
      });
    } catch (e) {
      out.push({
        id,
        error: String(e),
      });
    }
  }

  return res.json({
    results: out,
  });
});

// --------------------------------------------------
// Start server
// --------------------------------------------------

const PORT = Number(process.env.PORT || 8000);

if (!Number.isInteger(PORT) || PORT < 1 || PORT > 65535) {
  throw new Error("PORT must be an integer between 1 and 65535.");
}

app.listen(PORT, "0.0.0.0", () => {
  console.log(`Welcome page: http://localhost:${PORT}/`);
  console.log(`Application: http://localhost:${PORT}/app`);
});
