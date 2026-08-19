# Live Instance

Our demo of Numberline is [live here](http://32.195.157.101/).

## Prerequisites

Before running the project, make sure you have:

* Node.js
* npm

Check your installed versions:

```bash
node -v
npm -v
```

---

## Clone the Repository

```bash
git clone https://github.com/pubinv/number-spectra.git
cd number-spectra
```

---

## Install Dependencies

Install dependencies from the project root:

```bash
npm install
```

Then install dependencies for the `numberline` server:

```bash
cd numberline
npm install
```

Return to the project root:

```bash
cd ..
```

---

## How to Run the App

You need two terminal windows.

---

### Terminal 1 — Start the Numberline Server

From the project root:

```bash
cd numberline
node server.js
```

Keep this terminal running.

---

### Terminal 2 — Start the Frontend

Open a second terminal and go to the project root:

```bash
cd number-spectra
npm run dev
```

After the frontend starts, open the local URL shown in the terminal.

It is usually something like:

```text
http://localhost:5173
```

The exact port may be different depending on your setup.

---

## Typical Local Development Workflow

Start the backend server:

```bash
cd numberline
node server.js
```

Start the frontend from another terminal:

```bash
npm run dev
```

Then open the local development URL in your browser.

---

## Obsolete Demo

An older obsolete version is available here:

https://pubinv.github.io/number-spectra/numberline/index.html

This version may not reflect the current state of the project.

---

## Related Work

* http://thetimelineproj.sourceforge.net/about.html
* https://www.cs.ox.ac.uk/jeremy.gibbons/publications/rationals.pdf
* https://www.quora.com/How-could-we-create-a-bijection-between-the-algebraic-and-natural-numbers-What-would-be-the-5th-algebraic-number-using-that-enumeration
* https://en.wikipedia.org/wiki/Closed-form_expression#Closed-form_number
* https://oeis.org/wiki/Orderings_of_algebraic_numbers
* http://fredrikj.net/blog/2019/05/a-grimoire-of-functions/

