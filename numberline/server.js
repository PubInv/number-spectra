    // node server.js to start server
    import express from "express";

    const app = express();

    function toOeisId(raw) {
        // raw can be "40", "000040", "A40", "A000040"
        const s = String(raw).trim().toUpperCase().replace(/^A/, "");
        if (!/^\d+$/.test(s)) return null;

        // OEIS: 6 digits
        const padded = s.padStart(6, "0");
        return `A${padded}`;
    }

    function parseDataFieldToNumbers(dataStr) {
        // "2,3,5,7,11" -> [2,3,5,7,11]
        return String(dataStr)
            .split(",")
            .map(x => x.trim())
            .filter(Boolean)
            .map(x => Number(x))
            .filter(n => Number.isFinite(n));
    }

    app.get("/oeis/:anum", async (req, res) => {
        const id = toOeisId(req.params.anum);
        if (!id) return res.status(400).json({ error: "Bad input. Use digits like 40, 000040, or A000040." });

        const url = `https://oeis.org/${id}?fmt=json`;

        try {
            const r = await fetch(url, { headers: { "User-Agent": "oeis-proxy/1.0" } });
            if (!r.ok) return res.status(r.status).send(await r.text());

            const json = await r.json();

            // Handle both known shapes:
            // 1) top-level: { data: "..." }
            // 2) search-style: { results: [ { data: "..." } ] }
            const dataStr = json?.data ?? json?.results?.[0]?.data;

            if (!dataStr) {
                return res.status(502).json({
                    error: "Could not find a data field in OEIS response",
                    id,
                    gotKeys: Object.keys(json || {}),
                });
            }

            const numbers = parseDataFieldToNumbers(dataStr);

            res.set("Access-Control-Allow-Origin", "*"); // lock down later
            return res.json({ id, numbers, count: numbers.length });
        } catch (e) {
            return res.status(500).json({ error: String(e) });
        }
    });

    app.listen(3000, () => console.log("OEIS proxy running: http://localhost:3000"));
