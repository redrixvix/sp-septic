import { NextRequest, NextResponse } from "next/server";
import { Resend } from "resend";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { name, phone, email, address, service, message } = body;

    if (!name || !phone || !service) {
      return NextResponse.json(
        { success: false, message: "Name, phone, and service are required." },
        { status: 400 }
      );
    }

    const apiKey = process.env.RESEND_API_KEY;

    if (!apiKey) {
      // Graceful degradation — form still works but email won't send
      console.warn(
        "[S&P Septic] RESEND_API_KEY not set. Contact form submission logged but not emailed:",
        { name, phone, email, service, timestamp: new Date().toISOString() }
      );
      return NextResponse.json(
        {
          success: true,
          message: `Thank you, ${name}! We'll be in touch within 1 business day.`,
        },
        { status: 200 }
      );
    }

    const resend = new Resend(apiKey);

    const emailHtml = `
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <style>
    body { font-family: system-ui, -apple-system, sans-serif; color: #1e293b; margin: 0; padding: 0; }
    .container { max-width: 600px; margin: 0 auto; padding: 40px 20px; }
    .header { background: #1E3A5F; color: white; padding: 24px 32px; border-radius: 8px 8px 0 0; }
    .header h1 { margin: 0; font-size: 22px; font-weight: 800; }
    .header p { margin: 4px 0 0; opacity: 0.85; font-size: 14px; }
    .body { background: #f8fafc; border: 1px solid #e2e8f0; border-top: none; padding: 32px; border-radius: 0 0 8px 8px; }
    .field { margin-bottom: 18px; }
    .label { font-size: 11px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em; color: #64748b; margin-bottom: 4px; }
    .value { font-size: 16px; color: #0f172a; }
    .divider { border: none; border-top: 1px solid #e2e8f0; margin: 24px 0; }
    .footer { text-align: center; font-size: 12px; color: #94a3b8; margin-top: 32px; }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <h1>S&amp;P Septic &amp; Excavating</h1>
      <p>New Estimate Request — ${new Date().toLocaleDateString("en-US", { weekday: "long", year: "numeric", month: "long", day: "numeric" })}</p>
    </div>
    <div class="body">
      <div class="field">
        <div class="label">Name</div>
        <div class="value">${name}</div>
      </div>
      <div class="field">
        <div class="label">Phone</div>
        <div class="value"><a href="tel:${phone}">${phone}</a></div>
      </div>
      ${email ? `<div class="field"><div class="label">Email</div><div class="value"><a href="mailto:${email}">${email}</a></div></div>` : ""}
      <div class="field">
        <div class="label">Service Needed</div>
        <div class="value"><strong>${service}</strong></div>
      </div>
      ${address ? `<div class="field"><div class="label">Property Address</div><div class="value">${address}</div></div>` : ""}
      ${message ? `<div class="field"><div class="label">Message</div><div class="value" style="white-space: pre-wrap;">${message}</div></div>` : ""}
      <hr class="divider" />
      <div style="text-align: center;">
        <a href="tel:13309793930" style="background: #1E3A5F; color: white; padding: 12px 24px; border-radius: 6px; text-decoration: none; font-weight: 700; font-size: 16px;">📞 Call (330) 979-3930</a>
      </div>
    </div>
    <div class="footer">
      S&amp;P Septic &amp; Excavating Inc. · 2900 Elm Rd NE, Warren, OH 44483<br />
      <a href="https://spseptic.com" style="color: #94a3b8;">spseptic.com</a>
    </div>
  </div>
</body>
</html>
    `.trim();

    const { error } = await resend.emails.send({
      from: "S&P Septic Contact <onboarding@resend.dev>",
      to: ["info@spseptic.com"],
      replyTo: email || "noreply@spseptic.com",
      subject: `New Estimate Request from ${name} — ${service}`,
      html: emailHtml,
    });

    if (error) {
      console.error("[S&P Septic] Resend error:", error);
      return NextResponse.json(
        {
          success: false,
          message: "We couldn't send your message right now. Please call us directly at (330) 979-3930.",
        },
        { status: 500 }
      );
    }

    return NextResponse.json(
      {
        success: true,
        message: `Thank you, ${name}! We'll be in touch within 1 business day.`,
      },
      { status: 200 }
    );
  } catch (err) {
    console.error("[S&P Septic] Contact API error:", err);
    return NextResponse.json(
      {
        success: false,
        message: "An unexpected error occurred. Please try again or call us at (330) 979-3930.",
      },
      { status: 500 }
    );
  }
}
